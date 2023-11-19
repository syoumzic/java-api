package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;

import java.sql.SQLException;

public class DisableNotificationCommand extends AbstractCommand{
    /**
     * Выполняет определённые действия по завершении сбора данных
     *
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
