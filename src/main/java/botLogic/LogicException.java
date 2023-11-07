package botLogic;

/**
 * необходим для обработки некоректно введённых данных
 */
public class LogicException extends Exception{
    public LogicException(String message){
        super(message);
    }
}
