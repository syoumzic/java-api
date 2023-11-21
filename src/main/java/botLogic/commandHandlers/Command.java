package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;

/**
 * Интерфейс, предназначенный для обработки команды
 */
public interface Command {

    /**
     * Метод предназначенный для последовательной обработки необходимых параметров (дата, группа, ...)
     * @param user пользователь
     * @param message сообщение пользователя
     * @return сообщение успешного выполнения
     * @throws LogicException ошибка считывания параметров
     */
    String handle(User user, String message) throws LogicException;
}
