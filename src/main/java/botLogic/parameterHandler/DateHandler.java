package botLogic.parameterHandler;

import botLogic.utils.Calendar;
import botLogic.LogicException;
import botLogic.utils.Reference;
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

    /**
     * проверяет валидность введённой даты
     * @param message сообщение пользователя
     * @throws LogicException дата не валидна
     */
    public void handle(String message) throws LogicException {
        Calendar calendar = new Calendar();
        try{
            callbackDate.current = calendar.getLocalDate(message);
        }catch(DateTimeParseException e){
            throw new LogicException("Дата введена некорректно");
        }
    }
}
