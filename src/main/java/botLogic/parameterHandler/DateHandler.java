package botLogic.parameterHandler;

import botLogic.Calendar;
import botLogic.Reference;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateHandler implements ParameterHandler{
    private Reference<LocalDate>callbackDate;

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
            throw new RuntimeException("Дата введена некорректно");
        }
    }
}
