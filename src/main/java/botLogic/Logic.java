package botLogic;

import JavaBots.Bot;
import botLogic.dataBase.Data;
import botLogic.dataBase.Database;
import botLogic.parser.Parser;
import botLogic.parser.WebParser;
import botLogic.utils.Calendar;
import botLogic.utils.Time;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Класс для управления многопользовательской логикой
 */
public class Logic{
    private final Data dataBase;
    private final Parser parser;
    private final Time time;
    private Bot bot;
    private ScheduledExecutorService scheduler;

    private final HashMap<String, User>users = new HashMap<>();

    public Logic(Data dataBase, Parser parser, Time time, ScheduledExecutorService scheduler) {
        this.dataBase = dataBase;
        this.parser = parser;
        this.time = time;
        this.scheduler = scheduler;
    }

    /**
     * Инициализируем пользователей с уведомлениями
     */
    public void updateNotification(Bot bot){
        this.bot = bot;
        try {
            for (String id : dataBase.getUserIdNotification()) {
                User user;
                if (!users.containsKey(id)) {
                    user = new User(dataBase, id, parser, time, bot, scheduler);
                    users.put(id, user);
                }
                else{
                    user = users.get(id);
                }

                user.forceUpdateNotifications();
            }

            scheduler.schedule(() -> updateNotification(bot), time.getSecondsUtilTomorrow(), TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println(e.toString());
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
            user = new User(dataBase, id, parser, time, bot, scheduler);
            users.put(id, user);
        }

        return user.processMessage(message);
    }

}

