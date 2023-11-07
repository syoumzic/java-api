package botLogic.commandHandlers;

import botLogic.Calendar;
import botLogic.LogicException;
import botLogic.Reference;
import botLogic.User;
import botLogic.parameterHandler.DateHandler;
import botLogic.parameterHandler.ScheduleHandler;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ChangeScheduleCommand extends AbstractCommand {
    private Reference<LocalDate>date = new Reference<>();
    private Reference<List<String>>schedule = new Reference<>();

    public ChangeScheduleCommand(){
        setParameterHandlers(new DateHandler(date), new ScheduleHandler(schedule));
    }

    /**
     * Добавляет индивидуальное расписание
     * @param user текущий пользователь
     * @return сообщение успешного выполнения
     */
    protected String execute(User user) throws LogicException{
        try{
            user.getDatabase().getUsersGroup(user.getId());
        }catch(SQLException e){
            return "Для начала укажите свою группу";
        }

        Calendar calendar = new Calendar();
        try {
            user.getDatabase().setCastomSchedule(user.getId(),
                                                 schedule.current,
                                                 calendar.getShift(date.current));
        }catch(SQLException e){
            throw new LogicException("Внутренняя ошибка");
        }
        return "Расписание обновлено";
    }
}
