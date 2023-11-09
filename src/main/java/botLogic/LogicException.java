package botLogic;

/**
 * Ошибка о некорректности введённых данных
 */
public class LogicException extends Exception{

    /**
     * Конструктор класса LogicException
     * @param message сообщение ошибки
     */
    public LogicException(String message){
        super(message);
    }
}
