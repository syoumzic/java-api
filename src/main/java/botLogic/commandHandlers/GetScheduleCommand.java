package botLogic.commandHandlers;

import botLogic.Logic;
import botLogic.parameterHandler.GroupHandler;

import java.util.List;

public class GetScheduleCommand implements CommandHandler{
    public String action(Logic logic, String chatId){
        logic.changeParameterHandler(chatId, new GroupHandler());
        return "Укажите день";
    }
}
