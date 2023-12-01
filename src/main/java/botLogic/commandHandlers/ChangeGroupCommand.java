package botLogic.commandHandlers;

import botLogic.*;
import botLogic.parameterHandler.GroupHandler;
import botLogic.utils.Reference;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * команда /change_group
 */
public class ChangeGroupCommand extends AbstractCommand {
    Reference<String> group = new Reference<>();

    /**
     * Инициализация считывания группы
     */
    public ChangeGroupCommand(){
        setParameterHandlers(new GroupHandler(group));
    }

    /**
     * Меняет группу пользователя
     * @param user текущий пользователь
     * @return сообщение успешного выполнения
     */
    public String execute(User user) throws LogicException{
        try{
            user.getDatabase().addUserGroup(user.getId(), group.current);

             if(!user.getDatabase().tableIsExist(group.current.toLowerCase())){
                List<List<String>> weeksSchedule = user.getParser().parse(user.getTime(), user.getDatabase()
                        .getUsersGroup(user.getId())
                        .toUpperCase());

                user
                        .getDatabase()
                        .setSchedule(user.getDatabase().getUsersGroup(user.getId()), weeksSchedule);
            }

            user.getDatabase().deleteSchedule(user.getId(), 0);
        } catch (SQLException e) {
            throw new LogicException("Внутренняя ошибка", e);
        } catch (IOException e){
            throw new LogicException("Ошибка считывания расписания.", e);
        } catch (NoSuchElementException e){
            throw new LogicException("Не удалось найти группу с таким номером", e);
        }

        user.flushCommand();
        user.updateNotifications();

        return "Группа успешно обновлена!";
    }
}
