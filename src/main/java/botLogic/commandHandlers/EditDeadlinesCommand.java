package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;
import botLogic.parameterHandler.DateHandler;
import botLogic.parameterHandler.ListHandler;
import utils.Reference;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import utils.Time;

/**
 * Команда /edit_deadlines
 */
public class EditDeadlinesCommand extends AbstractCommand{
    private Reference<LocalDate>date = new Reference<>();
    private Reference<List<String>>deadlines = new Reference<>();
    private Time time;

    /**
     *  Установка считывания времени и дедлайнов
     */
    public EditDeadlinesCommand(Time time){
        this.time = time;
        setParameterHandlers(new DateHandler(date, time), new ListHandler(deadlines, time));
    }

    /**
     * Изменяет дедлайны на заданную дату
     * @param user текущий пользователь
     * @return сообщение об успешной применении команды
     * @throws LogicException ошибка выполнения команды
     */
    protected String execute(User user) throws LogicException, SQLException {
        try{
            user.editDeadlines(deadlines.current, time.getDateString(date.current));
        }catch(SQLException e){
            return "На этот день нет дедлайнов";
        }

        user.updateNotifications();
        return "Дедлайны успешно обновлены";
    }
}
