package botLogic.parameterHandler;

import botLogic.Calendar;
import botLogic.Reference;
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
import java.util.NoSuchElementException;

public class DateHandler implements ParameterHandler{
    Reference<LocalDate>callbackDate;

    public DateHandler(Reference<LocalDate> callbackDate){
        this.callbackDate = callbackDate;
    }

    public String startMessage(){
        return "Укажите день (например 1.12)";
    }

    public void handle(String message) throws RuntimeException{
        Calendar calendar = new Calendar();
        try{
            callbackDate.current = calendar.getLocalDate(message);
        }catch(DateTimeParseException e){
            throw new RuntimeException("введена некорректная дата");
        }
    }
}
