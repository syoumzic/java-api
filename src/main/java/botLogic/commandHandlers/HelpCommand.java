package botLogic.commandHandlers;

import botLogic.User;

/**
 * команда /help
 */
public class HelpCommand extends AbstractCommand {

    /**
     * Возвращает список команд
     * @param user текущий пользователь
     * @return список команд
     */
    protected String execute(User user){
        user.flushCommand();

        return  "Команды:\n" +
                "/help - вызов шпаргалки по возможностям бота\n" +
                "/change_group - изменить номер группы\n" +
                "/change_schedule - изменить расписание на день\n" +
                "/schedule - показ расписания на определённую дату\n" +
                "/next_lesson - ближайший предмет\n";
    }
}