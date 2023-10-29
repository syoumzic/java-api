package botLogic.parameterHandler;

import botLogic.User;

public class GroupHandler implements ParameterHandler {
    public String startMessage(){
        return "Укажите новый номер группы";
    }

    public String action(User user, String message){
        if(message.length() >= 3 && message.split(" ").length == 1){
            //data base moment
            user.setParameterHandler(null);
            return "Группа успешно добавлена!";
        }
        return "Группа введена некоректно";
    }
}
