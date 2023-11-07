package botLogic.parameterHandler;

import botLogic.LogicException;

public interface ParameterHandler {
    /**
     * указание на то, какие данные ожидаются
     */
    String startMessage();

    /**
     * обработка введённых параметров
     * @throws LogicException введены некоректные данные
     */
    void handle(String message) throws LogicException;
}
