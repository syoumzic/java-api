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
    public String execute(User user) throws LogicException, SQLException{
        user.addUserGroup(group.current);

        if(!user.scheduleExist()){
            try {
                List<List<String>> weekSchedule = user.loadSchedule();
                user.setSchedule(weekSchedule);
            }catch (IOException e){
                throw new LogicException("Не удалось прочесть расписание");
            }catch (NoSuchElementException e){
                throw new LogicException("Не удалось найти группу с таким номером");
            }
        }

        user.deleteScheule();

        user.flushCommand();
        user.updateNotifications();

        return "Группа успешно обновлена!";
    }
}
