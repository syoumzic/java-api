package botLogic;
import botLogic.parameterHandler.*;
import botLogic.commandHandlers.*;

import java.util.HashMap;

public class Logic{
    public Database dataBase;
    public HashMap<String, User>users = new HashMap<>();

    public String processMessage(String id, String message){
        User user = users.get(id);

        if(user == null) {
            user = new User(new GroupHandler());
            users.put(id, user);
        }

        return user.processMessage(message);
    }

}

