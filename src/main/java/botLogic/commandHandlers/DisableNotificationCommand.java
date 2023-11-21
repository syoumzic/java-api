package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;

import java.sql.SQLException;

/**
 * /notification_off
 */
public class DisableNotificationCommand extends AbstractCommand{
    /**
     * Выключить уведомления для конкретного пользователя
     * @param user текущий пользователь
     * @return сообщение об успешной применении команды
     * @throws LogicException ошибка выполнения команды
     */
    protected String execute(User user) throws LogicException {
        try {
            user.disableNotifications();
        }catch (SQLException e){
            return "Уведомления отключить не удалось";
        }

        return "Уведомления успешно удалены";
    }
}
