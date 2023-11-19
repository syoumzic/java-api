package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;

import java.sql.SQLException;
import java.time.DateTimeException;

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
