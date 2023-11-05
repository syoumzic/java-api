package botLogic.parameterHandler;

import botLogic.Reference;
import botLogic.User;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class GroupHandler implements ParameterHandler {
    Reference<String>group;

    public GroupHandler(Reference<String> group){
        this.group = group;
    }

    public String startMessage(){
        return "Укажите номер вашей группы";
    }

    public void handle(String message) throws RuntimeException{
        if(!Pattern.matches("^[A-Яа-я]+-[0-9]{6}$", message))
            throw new RuntimeException("Сообщение введено некорректно");

        group.current = message;
    }
}
