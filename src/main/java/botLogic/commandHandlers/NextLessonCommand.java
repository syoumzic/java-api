package botLogic.commandHandlers;

import botLogic.Calendar;
import botLogic.LogicException;
import botLogic.User;

import java.sql.SQLException;
import java.time.LocalDate;

public class NextLessonCommand extends AbstractCommand {
    protected String execute(User user) throws LogicException{
        Calendar calendar = new Calendar();
        try {
            return user.getDatabase().getNextLesson(user.getId(),
                                                    calendar.getShift(LocalDate.now()),
                                                    calendar.getMinute());
        }catch(SQLException e){
            throw new LogicException("Внутренняя ошибка");
        }
    }
}
