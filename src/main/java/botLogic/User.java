package botLogic;

import botLogic.commandHandlers.*;
import botLogic.parameterHandler.GroupHandler;
import botLogic.parameterHandler.ParameterHandler;

public class User {
    ParameterHandler parameterHandler;

    User(ParameterHandler parameterHandler){
        this.parameterHandler = parameterHandler;
    }

    public String processMessage(String message){
        message = message.trim();
        return message.startsWith("/")? commandHandle(message) : parameterHandle(message);
    }

    public String commandHandle(String message){
        return getCommandHandler(message).action(this);
    }

    private CommandHandler getCommandHandler(String message){
        return switch (message) {
            case "/start" -> new StartCommand();
            case "/help" -> new HelpCommand();
            case "/change group" -> new ChangeGroupCommand();
            case "/schedule" -> new GetScheduleCommand();
            default -> null;
        };
    }

    public String parameterHandle(String message){
        return parameterHandler.action(this, message);
    }

    public void setParameterHandler(ParameterHandler parameterHandler){
        this.parameterHandler = parameterHandler;
    }

    public void flushParameterHandler(){
        parameterHandler = null;
    }
}
