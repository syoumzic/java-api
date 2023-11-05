package botLogic;
import botLogic.parameterHandler.*;
import botLogic.commandHandlers.*;

import java.util.HashMap;

public class Logic{
    private final Data dataBase = new Database();
    private final HashMap<String, User>users = new HashMap<>();

    public String processMessage(String id, String message){
        User user = users.get(id);

        if(user == null) {
            user = new User(dataBase, id);
            users.put(id, user);
        }

        return user.processMessage(message);
    }

}

