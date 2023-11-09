package botLogic;

import botLogic.commandHandlers.*;
import botLogic.dataBase.Data;
import botLogic.parser.Parser;
import botLogic.utils.Time;

/**
 * Класс обрабатывающий сообщение пользователя
  */
public class User {
    private Command command = null;
    private Data dataBase;
    private String id;
    private Parser parser;
    private Time time;

    /**
     * Конструктор класса User
     * @param dataBase база данных
     * @param id id пользователя
     * @param parser считыватель расписания
     * @param time обработчик времени
     */
    User(Data dataBase, String id, Parser parser, Time time){
        this.dataBase = dataBase;
        this.id = id;
        this.parser = parser;
        this.time = time;
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
                if (command == null) return "комманда не найдена";

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

}
