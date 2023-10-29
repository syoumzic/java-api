package botLogic.parameterHandler;

import botLogic.Logic;

public interface ParameterHandler {
    public String action(Logic logic, String chatId, String message);
}
