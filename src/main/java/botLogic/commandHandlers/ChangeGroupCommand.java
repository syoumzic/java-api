package botLogic.commandHandlers;

import botLogic.User;
import botLogic.parameterHandler.GroupHandler;
import botLogic.parameterHandler.ParameterHandler;

public class ChangeGroupCommand implements CommandHandler{
    public String action(User user){
        ParameterHandler groupHandler = new GroupHandler();
        user.setParameterHandler(groupHandler);
        return groupHandler.startMessage();
    }
}
