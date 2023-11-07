package botLogic.commandHandlers;

import botLogic.utils.Calendar;
import botLogic.LogicException;
import botLogic.User;

import java.sql.SQLException;
import java.time.LocalDate;

public class NextLessonCommand extends AbstractCommand {

    /**
     * выдаёт ближайшую пару
     * @param user текущий пользователь
     * @return
     * @throws LogicException
     */
    protected String execute(User user) throws LogicException{
        try{
            user.getDatabase().getUsersGroup(user.getId());
        }catch(SQLException e){
            return "Для начала укажите свою группу";
        }

        try {
            return user.getDatabase().getNextLesson(user.getId(),
                                                    user.getTime().getShift(LocalDate.now()),
                                                    user.getTime().getMinute());
        }catch(SQLException e){
            throw new LogicException("Внутренняя ошибка");
        }
    }
}
