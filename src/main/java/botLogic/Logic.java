package botLogic;
import botLogic.parameterHandler.*;
import botLogic.commandHandlers.*;

import java.util.HashMap;

public class Logic{
    private final Data dataBase = new Database();
    private final HashMap<String, User>users = new HashMap<>();

    /**
     * обрабатывает сообщение для конкретного пользователя
     * @param id id пользователя
     * @param message сообщение пользователя
     * @return ответ на сообщение
     */
    public String processMessage(String id, String message){
        User user = users.get(id);

        if(user == null) {
            user = new User(dataBase, id);
            users.put(id, user);
        }

        return user.processMessage(message);
    }

}

