package utils;

import botLogic.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Обработчик уведомлений
 */
public class Notifications {
    private final List<ScheduledFuture<?>> notifications = new ArrayList<>();
    private final Time time;
    private final User user;
    private final ScheduledExecutorService scheduler;

    public Notifications(ScheduledExecutorService scheduler, User user, Time time){
        this.scheduler = scheduler;
        this.user = user;
        this.time = time;
    }

    /**
     * Установка уведомлений
     */
    public void setNotification(List<String>tusks, long notificationShift){
        for (String tusk : tusks) {
            if (tusk.equals("-")) continue;

            long lessonTime = time.getSecondsOfDay(tusk);
            long currentTime = time.getSecondsOfDay();

            if(lessonTime - notificationShift > currentTime)
                notifications.add(scheduler.schedule(() -> user.sendMessage(tusk), lessonTime - notificationShift - currentTime, TimeUnit.SECONDS));
        }
    }

    /**
     * Удаление уведомлений
     */
    public void removeNotification(){
        for (ScheduledFuture<?> notification : notifications)
            notification.cancel(true);
        notifications.clear();
    }
}
