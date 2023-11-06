package botLogic.commandHandlers;

import botLogic.Calendar;
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

    protected String execute(User user) {
        Calendar calendar = new Calendar();
        user.getDatabase().setCastomSchedule(user.getId(),
                schedule.current,
                new Calendar().getShift(date.current));
        return "Расписание обновлено";
    }
}
