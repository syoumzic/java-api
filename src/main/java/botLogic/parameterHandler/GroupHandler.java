package botLogic.parameterHandler;

import botLogic.LogicException;
import botLogic.User;
import botLogic.utils.Reference;
import java.util.regex.Pattern;

/**
 * Считыватель группы
 */
public class GroupHandler implements ParameterHandler {
    Reference<String>callbackGroup;

    /**
     * Конструктор класса GroupHandler
     * @param callbackGroup ссылка на группу в которую запишется считанное значение
     */
    public GroupHandler(Reference<String> callbackGroup){
        this.callbackGroup = callbackGroup;
    }

    public String startMessage(){
        return "Укажите номер вашей группы";
    }

    /**
     * Проверяет валидность введённой группы
     * @param message сообщение пользователя
     * @throws LogicException группа не валидна
     */
    public void handle(User user, String message) throws LogicException{
        if(!Pattern.matches("^[A-Яа-я]+-[0-9]{6}$", message))
            throw new LogicException("Группа введена некорректно");

        callbackGroup.current = message;
    }
}
