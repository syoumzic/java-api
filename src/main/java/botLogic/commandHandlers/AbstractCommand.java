package botLogic.commandHandlers;

import botLogic.User;
import botLogic.parameterHandler.ParameterHandler;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractCommand implements Command{
    private Queue<ParameterHandler> handlers = new LinkedList<>();
    private ParameterHandler currentParameter;

    /**
     * создаёт очередь из обработчиков
     * если не использовать очередь будет пустой
     * @param parameterHandlers массив обработчиков, использующихся последовательно в переданном порядке
     */
    protected void setParameterHandlers(ParameterHandler... parameterHandlers){
        for(ParameterHandler parameterHandler : parameterHandlers)
            handlers.add(parameterHandler);
    }

    /**
     * последовательно запускает обработчики
     * при успешном выполнении обработчика переходит на следующий
     * при некорректно введённых данных остаётся на текущем обработчике
     * если обработчики закончились, запускает функцию action реализованную в классах наследниках
     * @param user пользователь
     * @param message сообщение
     * @throws RuntimeException ошибка выполнения (любая: неправильный параметр, ошибка выполнения комманды)
     * @return ответ
     */
    public String handle(User user, String message) throws RuntimeException {
        if(currentParameter != null)
            currentParameter.handle(message);

        currentParameter = handlers.poll();          //next parameter

        return (currentParameter == null)? execute(user) : currentParameter.startMessage();
    }

    protected abstract String execute(User user) throws RuntimeException;
}
