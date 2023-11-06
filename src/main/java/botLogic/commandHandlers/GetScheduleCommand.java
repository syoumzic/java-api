package botLogic.commandHandlers;

import botLogic.*;
import botLogic.parameterHandler.DateHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

public class GetScheduleCommand extends AbstractCommand {
    private Reference<LocalDate>date = new Reference<>();

    public GetScheduleCommand(){
        setParameterHandlers(new DateHandler(date));
    }

    protected String execute(User user) throws RuntimeException{
        user.flushCommand();

        Calendar calendar = new Calendar();
        int numberDay = calendar.getShift(date.current) + 1;

        List<String> schedule = null;
        try{
            schedule = user.getDatabase().getSchedule(user.getId(), numberDay);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1146){
                try {
                    Parser parser = new WebParser();

                    List<List<String>> weeksShedule = parser.parse(user.getDatabase()
                                                                        .getUsersGroup(user.getId())
                                                                        .toUpperCase());

                    user
                        .getDatabase()
                        .setSchedule(user.getDatabase().getUsersGroup(user.getId()), weeksShedule);

                    schedule = user.getDatabase().getSchedule(user.getId(), numberDay);
                }catch(SQLException e){
                    throw new RuntimeException("Внутренняя ошибка");
                }catch (IOException e){
                    throw new RuntimeException("Ошибка считывания расписания. Попробуйте позже");
                } catch (NoSuchElementException e){
                    throw new RuntimeException("Не удалось найти группу с таким номером");
                }
            }
            else {
                System.out.println(ex.getMessage());
            }
        }

        if (schedule.isEmpty()) return "В этот день у вас нет пар";

        return toString(schedule);
    }

    private String toString(List<String>schedule){
        StringBuilder concat = new StringBuilder();
        for(String lesson : schedule)
            concat.append(lesson).append("\n");
        return concat.toString();
    }
}
