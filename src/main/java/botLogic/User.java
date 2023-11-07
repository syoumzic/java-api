package botLogic;

import botLogic.commandHandlers.*;

public class User {
    private Command command = null;
    private Data dataBase = null;
    private String id = null;

    User(Data dataBase, String id){
        this.dataBase = dataBase;
        this.id = id;
    }

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
            System.out.println(e.getMessage());
            return "Внутренняя ошибка";
        }
    }

    private boolean isCommand(String message){
        return message.startsWith("/");
    }

    public Data getDatabase(){
        return dataBase;
    }

    public String getId(){
        return id;
    }

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
