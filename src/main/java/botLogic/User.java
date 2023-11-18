package botLogic;

import JavaBots.Bot;
import botLogic.commandHandlers.*;
import botLogic.dataBase.Data;
import botLogic.parser.Parser;
import botLogic.utils.Time;

import java.io.IOException;
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

    public User(Data dataBase, String id, Parser parser, Time time, Bot bot) {
        this.dataBase = dataBase;
        this.id = id;
        this.parser = parser;
        this.time = time;
        this.bot = bot;
        this.command = null;
        this.notifications = new ArrayList<>();
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
                if (command == null) throw new LogicException("команда не найдена");

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

    /**
     * Возвращает обработчик команды
     * @param message название команды
     * @return обработчик команды
     */
    private Command getCommand(String message){
        return switch (message) {
            case "/help" -> new HelpCommand();
            case "/start" -> new StartCommand();
            case "/change_group" -> new ChangeGroupCommand();
            case "/change_schedule" -> new ChangeScheduleCommand();
            case "/schedule" -> new GetScheduleCommand();
            case "/next_lesson" -> new NextLessonCommand();
            default -> null;
        };
    }

    public void setCommand(Command command){
        this.command = command;
    }

    public void flushCommand(){
        this.command = null;
    }

    /**
     * Установка уведомлений для пользователя на день
     */
    public void setNotifications(ScheduledExecutorService scheduler) throws SQLException, IOException {
        List<String> schedule = dataBase.getSchedule(id, time.getShift(LocalDate.now()));

        for(String lesson : schedule){
            if(lesson.equals("-")) continue;

            int lessonTime = time.getTime(lesson) - dataBase.getNotificationShift(id);
            int notificationShift = dataBase.getNotificationShift(id);
            notifications.add(scheduler.schedule(() -> bot.sendMessage(Long.parseLong(id), lesson), lessonTime - notificationShift, TimeUnit.MINUTES));
        }
    }

    /**
     * Удаление уведомлений для пользователя на день
     */
    public void removeNotifications() {
        if (!notifications.isEmpty()) {
            for (ScheduledFuture<?> notification : notifications)
                notification.cancel(true);
            notifications.clear();
        }
    }
}
