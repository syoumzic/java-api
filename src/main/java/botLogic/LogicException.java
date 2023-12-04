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

    /**
     * Конструктор класса LogicException с указанием исходной ошибки
     * @param message сообщение ошибки
     */
    public LogicException(String message, Exception e){
        super(message);
        System.out.println(e.getMessage());
    }
}
