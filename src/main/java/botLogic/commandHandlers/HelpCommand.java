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
        return  "Команды:\n" +
                "/help - вызов шпаргалки по возможностям бота\n" +
                "/change_group - изменить номер группы\n" +
                "/change_schedule - изменить расписание на день\n" +
                "/schedule - показ расписания на определённую дату\n" +
                "/next_lesson - ближайший предмет на текущий день\n" +
                "/notification_on - включить уведомления\n" +
                "/notification_off - выключить уведомления\n" +
                "/notification_set - изменить время за которое необходимо предупредить о предстоящем предмете\n" +
                "/add_deadlines - добавить дедлайны на определённую дату\n" +
                "/edit_deadlines - изменить дедлайны на определённую дату\n" +
                "/next_deadlines - ближайший дедлайн на текущий день\n" +
                "/deadline_time - изменить время за которое необходимо предупредить о предстоящем дедлайне\n";
    }
}