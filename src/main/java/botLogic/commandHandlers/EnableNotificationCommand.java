package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.util.List;
import java.util.NoSuchElementException;

public class EnableNotificationCommand extends AbstractCommand{
    /**
     * Выполняет определённые действия по завершении сбора данных
     * @param user текущий пользователь
     * @return сообщение об успешной применении команды
     * @throws LogicException ошибка выполнения команды
     */
    protected String execute(User user) throws LogicException {
        user.flushCommand();

        try{
            user.getDatabase().getUsersGroup(user.getId());
        }catch (SQLException e){
            return "Для начала укажите группу";
        }

        try{
            user.enableNotifications();
        }catch (SQLException e){
            return "Уведомления включить не удалось";
        }catch (DateTimeException e){
            return "Не удалось считать расписание, попробуйте изменить его";
        }

        return "Уведомления включены";
    }
}
