package botLogic.parameterHandler;

import botLogic.User;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class GroupHandler implements ParameterHandler {
    public String startMessage(){
        return "Укажите номер вашей группы";
    }

    public String action(User user, String message){
        if(Pattern.matches("^[A-Яа-я]+-[0-9]{6}$", message)){
            user.getDatabase().addUserGroup(user.getId(), message);
            user.setParameterHandler(null);
            return "Группа успешно обновлена!";
        }
        return "Группа введена некоректно";
    }
}
