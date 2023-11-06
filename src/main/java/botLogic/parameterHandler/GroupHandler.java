package botLogic.parameterHandler;

import botLogic.Reference;
import java.util.regex.Pattern;

public class GroupHandler implements ParameterHandler {
    Reference<String>group;

    public GroupHandler(Reference<String> group){
        this.group = group;
    }

    public String startMessage(){
        return "Укажите номер вашей группы";
    }

    public void handle(String message) throws RuntimeException{
        if(!Pattern.matches("^[A-Яа-я]+-[0-9]{6}$", message))
            throw new RuntimeException("Группа введено некорректно");

        group.current = message;
    }
}
