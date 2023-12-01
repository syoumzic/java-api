package botLogic;

import JavaBots.Bot;
import botLogic.commandHandlers.*;
import botLogic.dataBase.Data;
import botLogic.parser.Parser;
import botLogic.utils.Time;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
    private final List<ScheduledFuture<?>>notifications;
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
        this.notifications = new ArrayList<>();
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
            return e.getMessage();
        }
        catch (Exception e){
            System.out.printf(e.getMessage());
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

    public Data getDatabase(){
        return dataBase;
    }

    public String getId(){
        return id;
    }

    public Parser getParser(){
        return parser;
    }

    public Time getTime(){
        return time;
    }

    public Bot getBot(){
        return bot;
    }

    public ScheduledExecutorService getScheduler(){
        return scheduler;
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
            case "/schedule" -> new GetScheduleCommand();
            case "/next_lesson" -> new NextLessonCommand(time);
            case "/notification_on" -> new EnableNotificationCommand();
            case "/notification_off" -> new DisableNotificationCommand();
            case "/notification_set" -> new SettingsNotificationCommand();
            case "/add_deadlines" -> new AddDeadlinesCommand(time);
            case "/edit_deadlines" -> new EditDeadlinesCommand(time);
            case "/next_deadlines" -> new NextDeadlinesCommand(time);
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
    public void updateNotifications(){
        try {
            if (dataBase.getStatusNotifications(id) == 1)
                forceUpdateNotifications();
        }catch (SQLException e){
            System.out.println(e.toString());
        }
    }

    /**
     * Реализация включений уведомления для пользователя на день
     */
    public void forceUpdateNotifications() throws SQLException{
        forceDisableNotifications();
        List<String> schedule = dataBase.getSchedule(id, time.getShift());
        for (String lesson : schedule) {
            if (lesson.equals("-")) continue;

            long lessonTime = time.getSecondsOfDay(lesson);
            long notificationShift = dataBase.getNotificationShift(id) * 60;
            long currentTime = time.getSecondsOfDay();

            if(lessonTime > currentTime)
                notifications.add(scheduler.schedule(() -> bot.sendMessage(Long.parseLong(id), lesson), lessonTime - notificationShift - currentTime, TimeUnit.SECONDS));
        }
    }

    /**
     * Реализация выключения уведомления для пользователя на день с сохранением в базу данных
     */
    public void disableNotifications() throws SQLException {
        forceDisableNotifications();
        dataBase.setStatusNotifications(id, 0);
    }

    /**
     * Реализация выключения уведомления для пользователя на день
     */
    public void forceDisableNotifications() {
        for (ScheduledFuture<?> notification : notifications)
            notification.cancel(true);
        notifications.clear();
    }
}
