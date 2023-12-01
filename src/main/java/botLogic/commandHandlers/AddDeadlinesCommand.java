package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;
import botLogic.parameterHandler.DateHandler;
import botLogic.parameterHandler.ListHandler;
import botLogic.utils.Reference;
import botLogic.utils.Time;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AddDeadlinesCommand extends AbstractCommand{
    Reference<LocalDate>date = new Reference<>();
    Reference<List<String>>deadlines = new Reference<>();
    Time time;

    public AddDeadlinesCommand(Time time) {
        this.time = time;
        setParameterHandlers(new DateHandler(date), new ListHandler(deadlines, time));
    }

    /**
     * Добавляет дедлайны на определённую дату
     * @param user текущий пользователь
     * @return сообщение об успешной применении команды
     * @throws LogicException ошибка выполнения команды
     */
    protected String execute(User user) throws LogicException, SQLException {
        user.getDatabase().setDeadlines(user.getId(), deadlines.current, time.getShift());
        return "Дедлайны успешно установлены";
    }
}
