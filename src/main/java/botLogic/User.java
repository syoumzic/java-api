package botLogic;

import botLogic.commandHandlers.*;
import botLogic.parameterHandler.NothingHandler;
import botLogic.parameterHandler.ParameterHandler;

public class User {
    private ParameterHandler parameterHandler;
    User(ParameterHandler parameterHandler){
        this.parameterHandler = parameterHandler;
    }

    public String processMessage(String message){
        message = message.trim();
        return message.startsWith("/")? processCommand(message) : processParameter(message);
    }

    public String processCommand(String message){
        var commandHandler = getCommandHandler(message);

        if(commandHandler == null) return "комманда не найдена!";
        return getCommandHandler(message).action(this);
    }

    private CommandHandler getCommandHandler(String message){
        return switch (message) {
            case "/start" -> new StartCommand();
            case "/help" -> new HelpCommand();
            case "/change_group" -> new ChangeGroupCommand();
            case "/schedule" -> new GetScheduleCommand();
            default -> null;
        };
    }

    public String processParameter(String message){
        return parameterHandler.action(this, message);
    }

    public void setParameterHandler(ParameterHandler parameterHandler){
        this.parameterHandler = parameterHandler;
    }

    public void flushParameterHandler(){
        parameterHandler = new NothingHandler();
    }
}