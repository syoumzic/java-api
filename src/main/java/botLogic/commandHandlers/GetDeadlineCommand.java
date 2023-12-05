package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;
import botLogic.parameterHandler.DateHandler;
import utils.Reference;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import utils.Time;

/**
 * Комана /deadlines
 */
public class GetDeadlineCommand extends AbstractCommand{
    Reference<LocalDate>date = new Reference<>();
    Time time;

    /**
     * Установка обработчика даты
     */
    public GetDeadlineCommand(Time time){
        this.time = time;
        setParameterHandlers(new DateHandler(date, time));
    }

    /**
     * Выдаёт дедлайны на определённый день
     */
    protected String execute(User user) throws LogicException, SQLException {
        List<String>deadlines;
        try {
            deadlines = user.getDeadlines(time.getDateString(date.current));
            if(deadlines.isEmpty())
                throw new SQLException();
        }catch (SQLException e){
            return "На текущий день нет дедлайнов";
        }

        return String.join("\n", deadlines) + "\n";
    }
}
