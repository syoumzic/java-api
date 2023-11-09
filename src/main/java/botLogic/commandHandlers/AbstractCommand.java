package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;
import botLogic.parameterHandler.ParameterHandler;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Базовый класс команд, который может обрабатывать параметры
 */
public abstract class AbstractCommand implements Command{
    private Queue<ParameterHandler> handlers = new LinkedList<>();
    private ParameterHandler currentParameter;

    /**
     * Создаёт очередь из обработчиков
     * @param parameterHandlers массив обработчиков, использующихся последовательно в переданном порядке
     */
    protected void setParameterHandlers(ParameterHandler... parameterHandlers){
        for(ParameterHandler parameterHandler : parameterHandlers)
            handlers.add(parameterHandler);
    }

    /**
     * Последовательно запрашивает необходимые данные (день, группа, ...)
     * @param user текущий пользователь
     * @param message сообщение от пользователя
     * @throws LogicException вызывается если в обработчике были введены некорректные данные
     * @return сообщение следующего обработчика
     */
    public String handle(User user, String message) throws LogicException {
        if(currentParameter != null)
            currentParameter.handle(user, message);

        currentParameter = handlers.poll();          //next parameter

        return (currentParameter == null)? execute(user) : currentParameter.startMessage();
    }

    /**
     * Выполняет определённые действия по завершении сбора данных
     * @param user текущий пользователь
     * @return сообщение об успешной применении команды
     * @throws LogicException ошибка выполнения команды
     */
    protected abstract String execute(User user) throws LogicException;
}
