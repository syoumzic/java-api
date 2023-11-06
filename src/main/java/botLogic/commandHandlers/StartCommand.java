package botLogic.commandHandlers;

import botLogic.Reference;
import botLogic.User;
import botLogic.parameterHandler.GroupHandler;
import botLogic.parameterHandler.ParameterHandler;

public class StartCommand extends Command {
    public String action(User user) throws RuntimeException{
        HelpCommand helpCommand = new HelpCommand();
        ChangeGroupCommand changeGroupCommand = new ChangeGroupCommand();

        String message = "Привет, я учебный бот УрФУ, мои возможности:\n" +
                         "Показывать расписание на определённую дату\n" +
                         helpCommand.action(user) +
                         "\n" +
                         changeGroupCommand.handle(user, "");

        user.setCommand(changeGroupCommand);

        return message;
    }
}
