package botLogic.commandHandlers;

import botLogic.User;

public class HelpCommand implements CommandHandler {
    public String action(User user){
        return "Привет, я учебный бот УрФУ, мои возможности:\n" +
                "Показывать расписание на определённую дату\n" +
                "Второстепенные команды:\n" +
                "/change - изменить номер группы\n" +
                "/help вызов шпаргалки по возможностям бота\n" +
                "/schedule - показ расписания на определённую дату\n";
    }
}