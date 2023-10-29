package botLogic;
import botLogic.parameterHandler.*;
import botLogic.commandHandlers.*;

import java.util.HashMap;

public class Logic{
    public Database dataBase;
    public HashMap<String, ParameterHandler> parameterHandlers = new HashMap<>();          //chatId -> user state

    public String processMessage(String chatId, String message){
        message = message.trim();
        return message.startsWith("/")? commandHandle(chatId, message) : parameterHandle(chatId, message);
    }

    public String commandHandle(String chatId, String message){
        return getCommandHandler(message).action(this, chatId);
    }

    private CommandHandler getCommandHandler(String message){
        return switch (message) {
            case "/start" -> new StartCommand();
            case "/help" -> new HelpCommand();
            default -> null;
        };
    }

    public String parameterHandle(String chatId, String message){
        ParameterHandler parameterHandler = getParameterHandler(chatId);
        return parameterHandler.action(this, chatId, message);
    }

    private ParameterHandler getParameterHandler(String chatId){
        ParameterHandler parameterHandler = parameterHandlers.get(chatId);
        if(parameterHandler == null){
            parameterHandler = new GroupHandler();
            changeParameterHandler(chatId, parameterHandler);
        }

        return parameterHandler;
    }

    public void changeParameterHandler(String chatId, ParameterHandler parameterHandler){
        parameterHandlers.put(chatId, parameterHandler);
    }

    public void flushParameterHandler(String chatId){
        parameterHandlers.put(chatId, null);
    }

}

