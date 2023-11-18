package botLogic.parameterHandler;

import botLogic.User;
import botLogic.utils.Time;
import botLogic.LogicException;
import botLogic.utils.Reference;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Считыватель даты
 */
public class DateHandler implements ParameterHandler{
    private Reference<LocalDate>callbackDate;

    /**
     * Конструктор класса DateHandler
     * @param callbackDate ссылка на дату в которую запишется считанное значение
     */
    public DateHandler(Reference<LocalDate> callbackDate){
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
        Time time = user.getTime();

        try{
            callbackDate.current = time.getLocalDate(message);
        }catch(IOException e){
            throw new LogicException("Дата введена некорректно");
        }
    }
}
