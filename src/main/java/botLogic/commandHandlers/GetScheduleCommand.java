package botLogic.commandHandlers;

import botLogic.*;
import botLogic.parameterHandler.DateHandler;
import botLogic.utils.Reference;

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

    /**
     * Установка считывания даты
     */
    public GetScheduleCommand(){
        setParameterHandlers(new DateHandler(date));
    }

    /**
     * Выдаёт расписание на день
     * @param user текущий пользователь
     * @return расписание
     */
    protected String execute(User user) throws LogicException{
        user.flushCommand();

        try{
            user.getDatabase().getUsersGroup(user.getId());
        }catch(SQLException e){
            throw new LogicException("Для начала укажите свою группу");
        }

        int numberDay = user.getTime().getShift(date.current);
        List<String> schedule = null;

        try{
            schedule = user.getDatabase().getSchedule(user.getId(), numberDay);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1146){
                try {

                    List<List<String>> weeksShedule = user.getParser().parse(user.getTime(), user.getDatabase().getUsersGroup(user.getId()).toUpperCase());

                    user
                        .getDatabase()
                        .setSchedule(user.getDatabase().getUsersGroup(user.getId()), weeksShedule);

                    schedule = user.getDatabase().getSchedule(user.getId(), numberDay);
                }catch(SQLException e){
                    throw new LogicException("Внутренняя ошибка", e);
                }catch (IOException e){
                    throw new LogicException("Ошибка считывания расписания.", e);
                } catch (NoSuchElementException e){
                    throw new LogicException("Не удалось найти группу с таким номером", e);
                }
            }
            else {
                System.out.println(ex.getMessage());
            }
        }

        if (schedule.isEmpty()) return "В этот день у вас нет пар";

        return String.join("\n", schedule) + "\n";
    }
}
