package botLogic.parameterHandler;

import botLogic.User;

import java.text.DateFormat;
import java.text.ParseException;
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
            //data base moment
            List<String>schedule = Arrays.asList("-", "матан", "матан");
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
        } catch (ParseException e) {
            return false;
        }

        return true;
    }
}
