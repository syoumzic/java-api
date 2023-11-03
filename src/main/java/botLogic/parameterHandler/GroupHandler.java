package botLogic.parameterHandler;

import botLogic.User;

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
            try {
                user.getDatabase().addUser(user.getId(), message);
            } catch (SQLException exep){
                int errNum = exep.getErrorCode();
                if (errNum == 1062){
                    try {
                        user.getDatabase().updateUser(user.getId(), message);
                    } catch (SQLException ex) {
                        System.out.println(ex.getErrorCode() + " " + ex.getMessage());
                    }catch (NoSuchElementException e){
                        return "группа не найдена!";
                    }
                    return "Группа обновлена";
                }
                else {
                    System.out.println(exep.getErrorCode() + " " + exep.getMessage());
                    return "Внутренняя ошибка";
                }

            }
            user.setParameterHandler(null);
            return "Группа успешно добавлена!";
        }
        return "Группа введена некоректно";
    }
}
