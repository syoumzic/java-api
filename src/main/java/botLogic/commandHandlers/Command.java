package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;

public interface Command {
    String handle(User user, String message) throws LogicException;
}
