package botLogic;

import JavaBots.Bot;
import dataBase.Data;
import parser.Parser;
import utils.Time;

import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Класс для управления многопользовательской логикой
 */
public class Logic{
    private final Data dataBase;
    private final Parser parser;
    private final Time time;
    private Bot tgBot;
    private Bot dsBot;
    private ScheduledExecutorService scheduler;

    private final HashMap<String, User>users = new HashMap<>();

    public Logic(Data dataBase, Parser parser, Time time, ScheduledExecutorService scheduler) {
        this.dataBase = dataBase;
        this.parser = parser;
        this.time = time;
        this.scheduler = scheduler;
    }

    public void setBots(Bot tgBot, Bot dsBot){
        this.tgBot = tgBot;
        this.dsBot = dsBot;
    }

    /**
     * Инициализируем пользователей с уведомлениями
     */
    public void updateNotification(){
        try {
            for (String id : dataBase.getUserIdNotification()) {
                User user;
                if (!users.containsKey(id)) {
                    if (id.charAt(0) == 't') {
                        user = new User(dataBase, id, parser, time, tgBot, scheduler);
                    } else {
                        user = new User(dataBase, id, parser, time, dsBot, scheduler);
                    }
                    users.put(id, user);
                }
                else{
                    user = users.get(id);
                }

                user.forceUpdateNotification();
            }

            scheduler.schedule(this::updateNotification, time.getSecondsUtilTomorrow(), TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
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
            if (id.charAt(0) == 't') {
                user = new User(dataBase, id, parser, time, tgBot, scheduler);
            } else {
                user = new User(dataBase, id, parser, time, dsBot, scheduler);
            }
            users.put(id, user);
        }

        return user.processMessage(message);
    }

}

