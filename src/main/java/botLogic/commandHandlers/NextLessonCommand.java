package botLogic.commandHandlers;

import botLogic.Calendar;
import botLogic.User;

import java.time.LocalDate;

public class NextLessonCommand extends AbstractCommand {
    protected String execute(User user){
        return user.getDatabase().getNextLesson(user.getId(), new Calendar().getShift(LocalDate.now()));
    }
}
