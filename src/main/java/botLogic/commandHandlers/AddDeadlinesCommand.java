package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;
import botLogic.parameterHandler.DateHandler;
import botLogic.parameterHandler.ListHandler;
import botLogic.utils.Reference;
import botLogic.utils.Time;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class AddDeadlinesCommand extends AbstractCommand{
    private Reference<LocalDate>date = new Reference<>();
    private Reference<List<String>>deadlines = new Reference<>();
    private ScheduledExecutorService scheduler;
    private Time time;

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

        List<String>nextDeadlines;
        try{
            nextDeadlines = user.getDeadlines(absoluteDate);
        }catch(SQLException e){
            nextDeadlines = new ArrayList<>();
        }

        nextDeadlines.addAll(deadlines.current);
        user.setDeadlines(nextDeadlines, absoluteDate);
        user.updateNotifications();

        return "Дедлайны успешно установлены";
    }
}
