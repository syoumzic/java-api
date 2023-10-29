package botLogic.parameterHandler;

import botLogic.Logic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

public class DateHandler {
    boolean dateIsCorrect(String message){
        final String dateFormat = "dd.MM";

        DateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(message);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    String action(Logic logic, String chatId, String message){
        if(dateIsCorrect(message)){
            logic.flushParameterHandler(chatId);
            List<String> schedule = logic.dataBase.getSchedule(chatId, 0);

            StringBuilder strSchedule = new StringBuilder();
            for(String lesson :schedule) strSchedule.append(lesson).append("\n");

            return strSchedule.toString();
        }

        return "дата введена некоректно!";
    }
}
