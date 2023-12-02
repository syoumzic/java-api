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
    protected String execute(User user) throws LogicException, SQLException {
        user.flushCommand();

        try{
            user.getUsersGroup();
        }catch (SQLException e){
            throw new LogicException("Для начала укажите группу");
        }

        user.setStatusNotification(1);
        user.forceUpdateNotifications();
        return "Уведомления включены";
    }
}
