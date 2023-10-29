package botLogic.commandHandlers;

import botLogic.Logic;
import botLogic.parameterHandler.ParameterHandler;

public interface CommandHandler {
    public String action(Logic logic, String chatId);
}
