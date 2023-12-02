package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;
import botLogic.parameterHandler.MinutesHandler;
import botLogic.utils.Reference;

import java.sql.SQLException;

/**
 * /notification_set
 */
public class SettingsNotificationCommand extends AbstractCommand{
    Reference<Integer>minutes = new Reference<Integer>();

    /**
     * Установка необходимого обработчика
     */
    public SettingsNotificationCommand(){
        setParameterHandlers(new MinutesHandler(minutes));
    }

    /**
     * Выполняет настройку времени
     *
     * @param user текущий пользователь
     * @return сообщение об успешной применении команды
     * @throws LogicException ошибка выполнения команды
     */
    protected String execute(User user) throws LogicException, SQLException{
        user.setNotificationShift(minutes.current);
        user.flushCommand();
        user.updateNotifications();

        return "Время установлено";
    }
}
