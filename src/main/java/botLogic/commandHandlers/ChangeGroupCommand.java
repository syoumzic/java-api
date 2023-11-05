package botLogic.commandHandlers;

import botLogic.Parser;
import botLogic.Reference;
import botLogic.User;
import botLogic.WebParser;
import botLogic.parameterHandler.GroupHandler;
import botLogic.parameterHandler.ParameterHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class ChangeGroupCommand extends Command {
    Reference<String>group = new Reference<>();

    public ChangeGroupCommand(){
        setParameterHandlers(new GroupHandler(group));
    }

    public String action(User user) throws RuntimeException{
        user.flushCommand();

        user.getDatabase().addUserGroup(user.getId(), group.current);
        try{
            user.getDatabase().tableIsExist(group.current.toLowerCase());
        } catch (SQLException ex) {
            try {
                Parser parser = new WebParser();
                List<List<String>> weeksSchedule = parser.parse(user.getDatabase()
                                                        .getUsersGroup(user.getId())
                                                        .toUpperCase());
                user
                    .getDatabase()
                    .setSchedule(user.getDatabase().getUsersGroup(user.getId()), weeksSchedule);
            }catch (IOException e){
                throw new RuntimeException("Ошибка считывания расписания. Попробуйте позже");
            } catch (NoSuchElementException e){
                throw new RuntimeException("Не удалось найти группу с таким номером");
            }
        }

        return "Группа успешно обновлена!";
    }
}
