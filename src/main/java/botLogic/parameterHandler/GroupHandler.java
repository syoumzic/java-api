package botLogic.parameterHandler;

import botLogic.Logic;

import java.util.regex.Pattern;

public class GroupHandler implements ParameterHandler {
    public String action(Logic logic, String chatId, String message){
        if(message.length() >= 3 && message.split(" ").length == 1){
            //logic.dataBase.updateUser(chatId, message);
            logic.changeParameterHandler(chatId, null);
            return "Группа успешно добавлена!";
        }
        return "Группа введена некоректно";
    }
}
