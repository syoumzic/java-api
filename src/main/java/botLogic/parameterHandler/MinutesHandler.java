package botLogic.parameterHandler;

import botLogic.LogicException;
import botLogic.User;
import botLogic.utils.Reference;

/**
 * Обработчик минут
 */
public class MinutesHandler implements ParameterHandler{
    Reference<Integer>callbackMinute;

    public MinutesHandler(Reference<Integer>callbackMinute){
        this.callbackMinute = callbackMinute;
    }

    /**
     * Указание к ожидаемым параметрам
     */
    public String startMessage() {
        return "Введите количество минут";
    }

    /**
     * Обработка введённого числа
     * @throws LogicException введены некорректные данные
     */
    public void handle(User user, String message) throws LogicException {
        Integer minutes;

        try{
            minutes = Integer.parseInt(message);
        }catch (NumberFormatException e){
            throw new LogicException("Введено не время");
        }

        if(minutes < 0 || minutes >= 90)
            throw new LogicException("Ожидается число от 0 до 90");

        callbackMinute.current = minutes;
    }
}
