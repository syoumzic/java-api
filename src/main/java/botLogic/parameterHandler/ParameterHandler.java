package botLogic.parameterHandler;

import botLogic.LogicException;
import botLogic.User;

/**
 * Интерфейс, для управления обработчиком
 */
public interface ParameterHandler {
    /**
     * Указание к ожидаемым параметрам
     */
    String startMessage();

    /**
     * Обработка введённых параметров
     * @throws LogicException введены некорректные данные
     */
    void handle(User user, String message) throws LogicException;
}
