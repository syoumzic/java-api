package botLogic.commandHandlers;

import botLogic.Logic;
import botLogic.parameterHandler.GroupHandler;

public class StartCommand implements CommandHandler {
    public String action(Logic logic, String chatId){
        HelpCommand helpCommand = new HelpCommand();
        logic.changeParameterHandler(chatId, new GroupHandler());
        return helpCommand.action(logic, chatId) +
               "для первоначальной настройки необходимо указать свою группу (например МЕН-220201).\n";
    }
}
