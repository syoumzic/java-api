package botLogic.commandHandlers;

import botLogic.Logic;
import botLogic.parameterHandler.GroupHandler;
import botLogic.parameterHandler.ParameterHandler;

public class ChangeGroupHandler implements CommandHandler{
    public String action(Logic logic, String chatId){
        logic.changeParameterHandler(chatId, new GroupHandler());
        return "Укажите новый номер группы";
    }
}
