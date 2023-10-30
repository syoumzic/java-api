package botLogic.commandHandlers;

import botLogic.User;

public class HelpCommand implements CommandHandler {
    public String action(User user){
        return  "Команды:\n" +
                "/change_group - изменить номер группы\n" +
                "/help вызов шпаргалки по возможностям бота\n" +
                "/schedule - показ расписания на определённую дату\n";
    }
}