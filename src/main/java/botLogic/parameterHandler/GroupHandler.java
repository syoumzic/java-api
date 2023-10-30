package botLogic.parameterHandler;

import botLogic.User;

import java.util.regex.Pattern;

public class GroupHandler implements ParameterHandler {
    public String startMessage(){
        return "Укажите номер вашей группы";
    }

    public String action(User user, String message){
        if(Pattern.matches("^[A-Яа-я]+-[0-9]{6}$", message)){
            //data base moment
            user.setParameterHandler(null);
            return "Группа успешно добавлена!";
        }
        return "Группа введена некоректно";
    }
}
