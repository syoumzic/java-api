package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * команда /next_lesson
 */
public class NextLessonCommand extends AbstractCommand {

    /**
     * выдаёт ближайшую пару
     * @param user текущий пользователь
     * @return ближайшая пара
     * @throws LogicException не удалось узнать ближайшую пару
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
                                                    user.getTime().getTime());
        }catch(SQLException e){
            throw new LogicException("Внутренняя ошибка");
        }
    }
}
