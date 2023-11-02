package botLogic.parameterHandler;

import botLogic.Calendar;
import botLogic.User;
import org.checkerframework.checker.units.qual.C;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class DateHandler implements ParameterHandler{
    public String startMessage(){
        return "Укажите день";
    }

    public String action(User user, String message){
        if(dateIsCorrect(message)){
            user.flushParameterHandler();
            Calendar calendar = new Calendar();
            int numberDay = calendar.getFirstDayOfEvenWeek() + 1;

            List<String> schedule = null;
            try{
                schedule = user.getDatabase().getSchedule(user.getId(), numberDay);
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
                        return "ошибка считывания расписания. Попробуйте позже";
                    }catch (IOException e){
                        System.out.println("ошибка соединения с интернетом");
                    }
                }
                else {
                    System.out.println(ex.getMessage());
                }
            }

            assert schedule != null;

            return toString(schedule);
        }

        return "дата введена некоректно!";
    }

    String toString(List<String>schedule){
        StringBuilder concat = new StringBuilder();
        for(String lesson : schedule)
                concat.append(lesson).append("\n");
        return concat.toString();
    }

    private boolean dateIsCorrect(String message){
        final String dateFormat = "dd.MM";

        DateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(message);
        } catch (java.text.ParseException e) {
            return false;
        }

        return true;
    }
}
