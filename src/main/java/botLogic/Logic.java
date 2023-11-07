package botLogic;
import botLogic.dataBase.Data;
import botLogic.dataBase.Database;
import botLogic.parser.Parser;
import botLogic.parser.WebParser;
import botLogic.utils.Calendar;
import botLogic.utils.Time;

import java.util.HashMap;

public class Logic{
    private final Data dataBase = new Database();
    private final Parser parser = new WebParser();
    private final Time time = new Calendar();
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
            user = new User(dataBase, id, parser, time);
            users.put(id, user);
        }

        return user.processMessage(message);
    }

}

