package botLogic.parameterHandler;

import botLogic.User;

public interface ParameterHandler {
    public String startMessage();
    public String action(User user, String message);
}
