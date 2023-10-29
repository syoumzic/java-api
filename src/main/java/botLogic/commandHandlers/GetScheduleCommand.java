package botLogic.commandHandlers;

import botLogic.User;
import botLogic.parameterHandler.DateHandler;
import botLogic.parameterHandler.GroupHandler;
import botLogic.parameterHandler.ParameterHandler;

public class GetScheduleCommand implements CommandHandler{
    public String action(User user){
        ParameterHandler dateHandler = new DateHandler();
        user.setParameterHandler(dateHandler);
        return dateHandler.startMessage();
    }
}
