package botLogic.parameterHandler;

import botLogic.LogicException;

public interface ParameterHandler {
    String startMessage();
    void handle(String message) throws LogicException;
}
