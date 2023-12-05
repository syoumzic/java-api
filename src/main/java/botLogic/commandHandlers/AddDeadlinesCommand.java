package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;
import botLogic.parameterHandler.DateHandler;
import botLogic.parameterHandler.ListHandler;
import utils.Reference;
import utils.Time;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * /add_deadlines
 */
public class AddDeadlinesCommand extends AbstractCommand{
    private Reference<LocalDate>date = new Reference<>();
    private Reference<List<String>>deadlines = new Reference<>();
    private ScheduledExecutorService scheduler;
    private Time time;

    /**
     * Установка считывания даты и списка
     */
    public AddDeadlinesCommand(ScheduledExecutorService scheduler, Time time) {
        this.scheduler = scheduler;
        this.time = time;
        setParameterHandlers(new DateHandler(date, time), new ListHandler(deadlines, time));
    }

    /**
     * Добавляет дедлайны на определённую дату
     * @param user текущий пользователь
     * @return сообщение об успешной применении команды
     * @throws LogicException ошибка выполнения команды
     */
    protected String execute(User user) throws LogicException, SQLException {
        String absoluteDate = time.getDateString(date.current);
        user.setDeadlines(deadlines.current, absoluteDate);
        user.updateNotifications();

        return "Дедлайны успешно установлены";
    }
}
