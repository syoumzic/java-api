package botLogic.commandHandlers;

import botLogic.User;

public interface Command {
    String handle(User user, String message) throws RuntimeException;
}
