package botLogic.commandHandlers;

import botLogic.*;
import botLogic.parameterHandler.DateHandler;
import botLogic.utils.Reference;
import botLogic.utils.Time;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * команда /schedule
 */
public class GetScheduleCommand extends AbstractCommand {
    private Reference<LocalDate> date = new Reference<>();
    private Time time;

    /**
     * Установка считывания даты
     */
    public GetScheduleCommand(Time time){
        this.time = time;
        setParameterHandlers(new DateHandler(date, time));
    }

    /**
     * Выдаёт расписание на день
     * @param user текущий пользователь
     * @return расписание
     */
    protected String execute(User user) throws LogicException, SQLException{
        user.flushCommand();

        try{
            user.getUsersGroup();
        }catch(SQLException e){
            throw new LogicException("Для начала укажите свою группу");
        }

        int numberDay = time.getShift(date.current);
        List<String> schedule = null;
        schedule = user.getSchedule(numberDay);

        if (schedule.isEmpty())
            return "В этот день у вас нет пар";

        return String.join("\n", schedule) + "\n";
    }
}
