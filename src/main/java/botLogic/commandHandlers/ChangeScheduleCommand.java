package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;
import botLogic.parameterHandler.DateHandler;
import botLogic.parameterHandler.ListHandler;
import utils.Reference;
import utils.Time;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * команда /change_schedule
 */
public class ChangeScheduleCommand extends AbstractCommand {
    private Reference<LocalDate>date = new Reference<>();
    private Reference<List<String>>schedule = new Reference<>();
    private Time time;

    /**
     * Установка считывания даты и расписания
     */
    public ChangeScheduleCommand(Time time){
        this.time = time;
        setParameterHandlers(new DateHandler(date, time), new ListHandler(schedule, time));
    }

    /**
     * Добавляет индивидуальное расписание
     * @param user текущий пользователь
     * @return сообщение успешного обновления расписания
     */
    protected String execute(User user) throws LogicException, SQLException {
        try{
            user.getUsersGroup();
        }catch(SQLException e){
            return "Для начала укажите свою группу";
        }

        user.setCastomSchedule(schedule.current, time.getShift(date.current));
        user.updateNotifications();

        return "Расписание обновлено";
    }
}
