package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;

import java.sql.SQLException;

/**
 * /notification_on
 */
public class EnableNotificationCommand extends AbstractCommand{
    /**
     * Включает уведомления
     * @param user текущий пользователь
     * @return сообщение об успешной применении команды
     * @throws LogicException ошибка выполнения команды
     */
    protected String execute(User user) throws LogicException {
        user.flushCommand();

        try{
            user.getDatabase().getUsersGroup(user.getId());
        }catch (SQLException e){
            return "Для начала укажите группу";
        }

        try {
            user.getDatabase().setStatusNotifications(user.getId(), 1);
            user.forceUpdateNotifications();
        }catch (SQLException e){
            return "внутренняя ошибка";
        }

        return "Уведомления включены";
    }
}
