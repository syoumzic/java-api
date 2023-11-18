package botLogic;

import JavaBots.Bot;
import botLogic.dataBase.Data;
import botLogic.dataBase.Database;
import botLogic.parser.Parser;
import botLogic.parser.WebParser;
import botLogic.utils.Calendar;
import botLogic.utils.Time;

import java.io.IOException;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Класс для управления многопользовательской логикой
 */
public class Logic{
    private final Data dataBase = new Database();
    private final Parser parser = new WebParser();
    private final Time time = new Calendar();
    private final HashMap<String, User>users = new HashMap<>();
    private Bot bot;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);;
    private ScheduledFuture<?> updateNotificationTask;

    /**
     * Инициализируем пользователей с уведомлениями
     */
    public void updateNotification(Bot bot){
        this.bot = bot;
        try {
            for (String id : dataBase.getUserIdNotification()) {
                User user;
                if (!users.containsKey(id)) {
                    user = new User(dataBase, id, parser, time, bot);
                    users.put(id, user);
                }
                else{
                    user = users.get(id);
                }

                user.setNotifications(scheduler);
            }

            updateNotificationTask = scheduler.schedule(() -> updateNotification(bot), time.utilTomorrow(), TimeUnit.MINUTES);
        }catch(SQLException e){
            System.out.println("Уведомления установить не удалось");
        }catch(IOException e){
            System.out.println("Не удалось считать расписание");
        }
    }

    /**
     * Обрабатывает сообщение для конкретного пользователя
     * @param id id пользователя
     * @param message сообщение пользователя
     * @return ответ на сообщение
     */
    public String processMessage(String id, String message){
        User user = users.get(id);

        if(user == null) {
            user = new User(dataBase, id, parser, time, bot);
            users.put(id, user);
        }

        return user.processMessage(message);
    }

}

