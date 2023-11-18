package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;
import botLogic.parameterHandler.DateHandler;
import botLogic.parameterHandler.ScheduleHandler;
import botLogic.utils.Reference;

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
    public ChangeScheduleCommand(){
        setParameterHandlers(new DateHandler(date), new ScheduleHandler(schedule));
    }

    /**
     * Добавляет индивидуальное расписание
     * @param user текущий пользователь
     * @return сообщение успешного обновления расписания
     */
    protected String execute(User user) throws LogicException{
        try{
            user.getDatabase().getUsersGroup(user.getId());
        }catch(SQLException e){
            return "Для начала укажите свою группу";
        }

        try {
            user.getDatabase().setCastomSchedule(user.getId(),
                                                 schedule.current,
                                                 user.getTime().getShift(date.current));
        }catch(SQLException e){
            throw new LogicException("Внутренняя ошибка");
        }
        return "Расписание обновлено";
    }
}
