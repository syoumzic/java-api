package botLogic.parameterHandler;

import botLogic.LogicException;
import botLogic.User;
import botLogic.utils.Reference;

/**
 * Обработчик диапазона
 */
public class RangeHandler implements ParameterHandler{
    private final Reference<Integer>callbackMinute;
    private final String startMessage;
    private final int max;

    public RangeHandler(String startMessage, int max, Reference<Integer>callbackMinute){
        this.startMessage = startMessage;
        this.max = max;
        this.callbackMinute = callbackMinute;
    }

    /**
     * Указание к ожидаемым параметрам
     */
    public String startMessage() {
        return startMessage;
    }

    /**
     * Обработка введённого числа
     * @throws LogicException введены некорректные данные
     */
    public void handle(User user, String message) throws LogicException {
        int number;

        try{
            number = Integer.parseInt(message);
        }catch (NumberFormatException e){
            throw new LogicException("Введено не число");
        }

        if(number < 0 || number > max)
            throw new LogicException("Ожидается число от 0 до %d".formatted(max));

        callbackMinute.current = number;
    }
}
