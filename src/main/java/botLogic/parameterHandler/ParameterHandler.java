package botLogic.parameterHandler;

import botLogic.LogicException;
import botLogic.User;

public interface ParameterHandler {
    /**
     * указание на то, какие данные ожидаются
     */
    String startMessage();

    /**
     * обработка введённых параметров
     * @throws LogicException введены некоректные данные
     */
    void handle(User user, String message) throws LogicException;
}
