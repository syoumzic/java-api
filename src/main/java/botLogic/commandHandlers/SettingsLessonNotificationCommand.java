package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;
import botLogic.parameterHandler.RangeHandler;
import utils.Reference;

import java.sql.SQLException;

/**
 * /notification_set
 */
public class SettingsLessonNotificationCommand extends AbstractCommand{
    private Reference<Integer>minutes = new Reference<>();

    /**
     * Установка обработчика минут
     */
    public SettingsLessonNotificationCommand(){
        final int max = 90;
        setParameterHandlers(new RangeHandler("введите количество минут (от 0 до %d)".formatted(max), max, minutes));
    }

    /**
     * Выполняет настройку времени за которое необходимо предупредить о предстоящей паре
     */
    protected String execute(User user) throws LogicException, SQLException{
        user.setNotificationShift(minutes.current);
        user.updateNotifications();

        return "Время установлено";
    }
}
