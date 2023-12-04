package botLogic;

import JavaBots.Bot;
import botLogic.commandHandlers.*;
import dataBase.Data;
import parser.Parser;
import utils.Notifications;
import utils.Time;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Класс обрабатывающий сообщение пользователя
  */
public class User {
    private Command command;
    private final Data dataBase;
    private final String id;
    private final Parser parser;
    private final Time time;
    private final Bot bot;
    private Notifications lessonNotifications;
    private Notifications deadlineNotifications;
    private final ScheduledExecutorService scheduler;

    /**
     * Конструктор класса
     * @param parser считыватель онлайн расписания
     * @param scheduler установщик задач
     */
    public User(Data dataBase, String id, Parser parser, Time time, Bot bot, ScheduledExecutorService scheduler) {
        this.dataBase = dataBase;
        this.id = id;
        this.parser = parser;
        this.time = time;
        this.bot = bot;
        this.command = null;
        this.lessonNotifications = new Notifications(scheduler, this, time);
        this.deadlineNotifications = new Notifications(scheduler, this, time);
        this.scheduler = scheduler;
    }

    /**
     * Обрабатывает сообщение от пользователя
     * @param message сообщение пользователя
     * @return ответ на сообщение
     */
    public String processMessage(String message){
        message = message.trim();

        try {
            if (isCommand(message)) {
                command = getCommand(message);
                return command.handle(this, message);
            }

            if (command != null)
                return command.handle(this, message);

            return new HelpCommand().handle(this, "");
        }
        catch(LogicException e){
            return e.getLocalizedMessage();
        }
        catch (Exception e){
            System.out.printf(e.getLocalizedMessage());
            return "Внутренняя ошибка";
        }
    }

    /**
     * Проверяет сообщение на команду
     * @param message сообщение пользователя
     * @return является ли сообщение командой
     */
    private boolean isCommand(String message){
        return message.startsWith("/");
    }

    /**
     * Возвращает обработчик команды
     * @param message название команды
     * @return обработчик команды
     */
    private Command getCommand(String message) throws LogicException{
        return switch (message) {
            case "/help" -> new HelpCommand();
            case "/start" -> new StartCommand();
            case "/change_group" -> new ChangeGroupCommand();
            case "/change_schedule" -> new ChangeScheduleCommand(time);
            case "/schedule" -> new GetScheduleCommand(time);
            case "/next_lesson" -> new NextLessonCommand(time);
            case "/notification_on" -> new EnableNotificationCommand();
            case "/notification_off" -> new DisableNotificationCommand();
            case "/notification_set" -> new SettingsLessonNotificationCommand();
            case "/add_deadlines" -> new AddDeadlinesCommand(scheduler, time);
            case "/edit_deadlines" -> new EditDeadlinesCommand(time);
            case "/next_deadlines" -> new NextDeadlinesCommand(time);
            case "/deadline_time" -> new SettingsDeadlinesNotificationCommand();
            default -> throw new LogicException("Команда не найдена");
        };
    }

    public void setCommand(Command command){
        this.command = command;
    }

    public void flushCommand(){
        this.command = null;
    }

    /**
     * Включение уведомлений для пользователя на день с сохранением в базу данных
     */
    public void updateNotifications() throws SQLException{
        if (getStatusNotifications() == 1){
            forceUpdateNotification();
        }
    }

    public void forceUpdateNotification() throws SQLException{
        lessonNotifications.setNotification(getSchedule(time.getShift()), getLessonNotificationsShift() * 60L);
        try {
            lessonNotifications.setNotification(getDeadlines(time.getDateString()), getDeadlinesNotificationsShift() * 3600L);
        }
        catch (SQLException ignored){ }
    }

    /**
     * Реализация выключения уведомления для пользователя на день с сохранением в базу данных
     */
    public void disableNotifications() throws SQLException {
        lessonNotifications.removeNotification();
        deadlineNotifications.removeNotification();
    }

    public void sendMessage(String message){
        bot.sendMessage(Long.parseLong(id.substring(1)), message);
    }

    public List<List<String>> loadSchedule() throws NoSuchElementException, IOException, SQLException {
        return parser.parse(time, dataBase.getUsersGroup(id));
    }

    /**
     * Метод базы данных getSchedule для данного пользователя
     */
    public List<String> getSchedule(int day) throws SQLException{
        return dataBase.getSchedule(id, day);
    }

    /**
     * Метод базы данных setSchedule для данного пользователя
     */
    public void setSchedule(List<List<String>>schedule) throws SQLException{
        dataBase.setSchedule(id, schedule);
    }

    /**
     * Метод базы данных addUserGroup для данного пользователя
     */
    public void addUserGroup(String group) throws SQLException{
        dataBase.addUserGroup(id, group);
    }

    /**
     * Метод базы данных getUserGroup для данного пользователя
     */
    public String getUsersGroup() throws SQLException{
        return dataBase.getUsersGroup(id);
    }

    /**
     * Метод базы данных setDeadlines для данного пользователя
     */
    public void setDeadlines(List<String>deadlines, String day) throws SQLException{
        dataBase.setDeadlines(id, deadlines, day);
    }

    /**
     * Метод базы данных getDeadlines для данного пользователя
     */
    public List<String> getDeadlines(String day) throws SQLException{
        return dataBase.getDeadlines(id, day);
    }

    /**
     * Метод базы данных getStatusNotification для данного пользователя
     */
    public void setStatusNotifications(int status) throws SQLException{
        dataBase.setStatusNotifications(id, status);
    }

    /**
     * Метод базы данных getStatusNotification для данного пользователя
     */
    public void setNotificationShift(int minutes) throws SQLException{
        dataBase.setNotificationShift(id, minutes);
    }

    /**
     *
     */
    public void editDeadlines(List<String>deadlines, String date) throws SQLException {
        dataBase.editDeadlines(id, deadlines, date);
    }

    public void setCastomSchedule(List<String> current, int shift) throws SQLException{
        dataBase.setCastomSchedule(id, current, shift);
    }

    public int getLessonNotificationsShift() throws SQLException{
        return dataBase.getNotificationShift(id);
    }

    public int getDeadlinesNotificationsShift() throws SQLException{
        return dataBase.getDeadlineNotificationShift(id);
    }

    public void setDeadlineNotificationShift(int hours) throws SQLException {
        dataBase.setDeadlineNotificationShift(id, hours);
    }

    public void deleteScheule() {

    }

    private int getStatusNotifications() throws SQLException{
        return dataBase.getStatusNotifications(id);
    }

}
