package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import botLogic.utils.Time;


public class NextDeadlinesCommand extends AbstractCommand {
    Time time;

    public NextDeadlinesCommand(Time time){
        this.time = time;
    }

    /**
     * Выводит ближайший дедлайн
     * @param user текущий пользователь
     * @return сообщение об успешной применении команды
     * @throws LogicException ошибка выполнения команды
     */
    public String execute(User user) throws LogicException, SQLException{
        HashMap<String, List<String>> deadlines = user.getAllDeadlines();

        int currentSeconds = time.getSecondsOfDay();
        for(String day : deadlines.keySet())
            for(String deadline : deadlines.get(day))
                if(currentSeconds < time.getSecondsOfDay(deadline))
                    return deadline;

        return "Дедлайнов нет";
    }
}
