package botLogic.parameterHandler;

import botLogic.User;
import utils.Time;
import botLogic.LogicException;
import utils.Reference;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Считыватель даты
 */
public class DateHandler implements ParameterHandler{
    private Reference<LocalDate>callbackDate;
    private Time time;
    /**
     * Конструктор класса DateHandler
     * @param callbackDate ссылка на дату в которую запишется считанное значение
     */
    public DateHandler(Reference<LocalDate> callbackDate, Time time){
        this.time = time;
        this.callbackDate = callbackDate;
    }

    public String startMessage(){
        return "Укажите день (например 1.12)";
    }

    /**
     * Проверяет валидность введённой даты
     * @param message сообщение пользователя
     * @throws LogicException дата не валидна
     */
    public void handle(User user, String message) throws LogicException {
        try{
            callbackDate.current = time.getLocalDate(message);
        }catch(DateTimeException e){
            throw new LogicException("Дата введена некорректно");
        }
    }
}
