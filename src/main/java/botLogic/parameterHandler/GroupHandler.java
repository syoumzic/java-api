package botLogic.parameterHandler;

import botLogic.LogicException;
import botLogic.User;
import botLogic.utils.Reference;
import java.util.regex.Pattern;

public class GroupHandler implements ParameterHandler {
    Reference<String>group;

    public GroupHandler(Reference<String> group){
        this.group = group;
    }

    public String startMessage(){
        return "Укажите номер вашей группы";
    }

    /**
     * проверяет валидность введённой группы
     * @param message сообщение пользователя
     * @throws LogicException группа не валидна
     */
    public void handle(User user, String message) throws LogicException{
        if(!Pattern.matches("^[A-Яа-я]+-[0-9]{6}$", message))
            throw new LogicException("Группа введена некорректно");

        group.current = message;
    }
}
