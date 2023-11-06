package botLogic.commandHandlers;

import botLogic.User;

public class StartCommand extends AbstractCommand {
    public String execute(User user) throws RuntimeException{
        Command helpCommand = new HelpCommand();
        Command changeGroupCommand = new ChangeGroupCommand();

        String message = "Привет, я учебный бот УрФУ, мои возможности:\n" +
                         "Показывать расписание на определённую дату\n" +
                         helpCommand.handle(user, "") +
                         "\n" +
                         changeGroupCommand.handle(user, "");

        user.setCommand(changeGroupCommand);

        return message;
    }
}
