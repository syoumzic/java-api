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

/**
 * команда /change_schedule
 */
public class ChangeScheduleCommand extends AbstractCommand {
    private Reference<LocalDate>date = new Reference<>();
    private Reference<List<String>>schedule = new Reference<>();

    /**
     * Установка считывания даты и расписания
     */
    public ChangeScheduleCommand(Time time){
        setParameterHandlers(new DateHandler(date), new ListHandler(schedule, time));
    }

    /**
     * Добавляет индивидуальное расписание
     * @param user текущий пользователь
     * @return сообщение успешного обновления расписания
     */
    protected String execute(User user) throws LogicException, SQLException {
        try{
            user.getDatabase().getUsersGroup(user.getId());
        }catch(SQLException e){
            return "Для начала укажите свою группу";
        }

        user.getDatabase().setCastomSchedule(user.getId(),
                                             schedule.current,
                                             user.getTime().getShift(date.current));

        user.flushCommand();
        user.updateNotifications();

        return "Расписание обновлено";
    }
}
