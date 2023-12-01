package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;
import botLogic.parameterHandler.DateHandler;
import botLogic.parameterHandler.ListHandler;
import botLogic.utils.Reference;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import botLogic.utils.Time;

public class EditDeadlinesCommand extends AbstractCommand{
    Reference<LocalDate>date = new Reference<>();
    Reference<List<String>>deadlines = new Reference<>();
    Time time;

    /**
     *  Установка считывания времени и дедлайнов
     */
    public EditDeadlinesCommand(Time time){
        setParameterHandlers(new DateHandler(date), new ListHandler(deadlines, time));
    }

    /**
     * Изменяет дедлайны на заданную дату
     * @param user текущий пользователь
     * @return сообщение об успешной применении команды
     * @throws LogicException ошибка выполнения команды
     */
    protected String execute(User user) throws LogicException, SQLException {
        try{
            user.getDatabase().getDeadlines(user.getId(), time.getShift());
        }catch(SQLException e){
            return "На этот день нет дедлайнов";
        }

        return "Дедлайны успешно обновлены";
    }
}
