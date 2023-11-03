package botLogic.parameterHandler;

import botLogic.Calendar;
import botLogic.User;
import org.checkerframework.checker.units.qual.C;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class DateHandler implements ParameterHandler{
    public String startMessage(){
        return "Укажите день (например 01.12)";
    }

    public String action(User user, String message){
        Calendar calendar = new Calendar();
        int numberDay;
        try {
            numberDay = calendar.getFirstDayOfEvenWeek(message);
        } catch (DateTimeParseException e) {
            return "Введена некорректная дата";
        }

        List<String> schedule = null;
        try{
            schedule = user.getDatabase().getSchedule(user.getId(), numberDay + 1);
        } catch (SQLException ex) {
            int errNum = ex.getErrorCode();
            if (errNum == 1146){
                try {
                    List<List<String>> schedule_pars = user
                            .getWebParser()
                            .parse(user.getDatabase()
                                    .getUsersGroup(user.getId())
                                    .toUpperCase());
                    user
                        .getDatabase()
                        .setSchedule(user.getDatabase().getUsersGroup(user.getId()), schedule_pars);
                    schedule = user.getDatabase().getSchedule(user.getId(), numberDay);

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } catch (ParseException e){
                    return "Ошибка считывания расписания. Попробуйте позже";
                }catch (IOException e){
                    System.out.println("Ошибка соединения с интернетом");
                }
            }
            else {
                System.out.println(ex.getMessage());
            }
        }

        if (schedule.isEmpty()) return "В этот день у вас нет пар";
        user.flushParameterHandler();

        return toString(schedule);
    }

    String toString(List<String>schedule){
        StringBuilder concat = new StringBuilder();
        for(String lesson : schedule)
                concat.append(lesson).append("\n");
        return concat.toString();
    }

}
