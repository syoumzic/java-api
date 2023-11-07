package botLogic;

import botLogic.commandHandlers.*;
import botLogic.dataBase.Data;
import botLogic.parser.Parser;
import botLogic.utils.Time;

public class User {
    private Command command = null;
    private Data dataBase;
    private String id;
    private Parser parser;
    private Time time;

    User(Data dataBase, String id, Parser parser, Time time){
        this.dataBase = dataBase;
        this.id = id;
        this.parser = parser;
        this.time = time;
    }

    /**
     * обрабатывает сообщение от пользователя
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
     * проверяет является ли сообщение командой
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
     * Возвращает обработчик комманды
     * @param message название команды
     * @return обработчик комманды
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
