package botLogic.commandHandlers;

import botLogic.LogicException;
import botLogic.User;

import java.sql.SQLException;
import java.util.List;
import utils.Time;


public class NextDeadlinesCommand extends AbstractCommand {
    final private Time time;

    public NextDeadlinesCommand(Time time){
        this.time = time;
    }

    /**
     * Выводит ближайший дедлайн на день
     */
    public String execute(User user) throws LogicException, SQLException{
        List<String>deadlines = user.getDeadlines(time.getDateString());
        long currentTime = time.getSecondsOfDay();

        for(String deadline : deadlines){
            long deadlineTime = time.getSecondsOfDay(deadline);

            if(currentTime < deadlineTime)
                return deadline;
        }

        return "Дедлайнов на сегодня нет";
    }
}
