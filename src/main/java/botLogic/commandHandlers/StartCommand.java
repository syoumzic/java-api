package botLogic.commandHandlers;

import botLogic.User;
import botLogic.parameterHandler.GroupHandler;
import botLogic.parameterHandler.ParameterHandler;

public class StartCommand implements CommandHandler {
    public String action(User user){
        HelpCommand helpCommand = new HelpCommand();
        ParameterHandler groupHandler = new GroupHandler();
        user.setParameterHandler(groupHandler);
        return helpCommand.action(user) +
               groupHandler.startMessage();
    }
}
