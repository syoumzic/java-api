package botLogic.commandHandlers;

import botLogic.User;
import botLogic.parameterHandler.ParameterHandler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Command {
    Queue<ParameterHandler> scanner = new LinkedList<>();
    ParameterHandler currentParameter;

    void setParameterHandlers(ParameterHandler... parameterHandlers){
        scanner = new LinkedList<>(Arrays.asList(parameterHandlers));
    }

    public String handle(User user, String message) throws RuntimeException{
        if(currentParameter != null)
            currentParameter.handle(message);

        currentParameter = scanner.poll();          //next parameter

        return (currentParameter == null)? action(user) : currentParameter.startMessage();
    }

    public abstract String action(User user);
}
