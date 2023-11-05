package botLogic.parameterHandler;

import botLogic.User;
import botLogic.commandHandlers.HelpCommand;

public class NothingHandler implements ParameterHandler{
    public String startMessage(){ return ""; }
    public String action(User user, String message){
        return new HelpCommand().action(user);
    }
}
