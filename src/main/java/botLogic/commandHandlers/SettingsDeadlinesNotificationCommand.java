package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;
import botLogic.parameterHandler.RangeHandler;
import botLogic.utils.Reference;

import java.sql.SQLException;

public class SettingsDeadlinesNotificationCommand extends AbstractCommand{
    Reference<Integer> hours = new Reference<>();

    /**
     * Установка обработчика часов
     */
    public SettingsDeadlinesNotificationCommand() {
        final int max = 24;
        setParameterHandlers(new RangeHandler("введите количество часов (от 0 до %d)".formatted(max), max, hours));
    }

    /**
     * Выполняет настройку времени за которое необходимо предупредить о предстоящем дедлайн
     */
    protected String execute(User user) throws LogicException, SQLException {
        user.setDeadlineNotificationShift(hours.current);
        user.updateNotifications();

        return "Время установлено";
    }
}
