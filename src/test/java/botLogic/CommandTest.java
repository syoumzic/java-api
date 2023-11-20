package botLogic;

import JavaBots.Bot;
import botLogic.dataBase.Data;
import botLogic.dataBase.Database;
import botLogic.parser.Parser;
import botLogic.parser.WebParser;
import botLogic.utils.Calendar;
import botLogic.utils.Time;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Проверка обработки команд
 */
@ExtendWith(MockitoExtension.class)
public class CommandTest {
    @Mock
    Data database;

    @Mock
    Parser parser;

    @Mock
    ScheduledExecutorService scheduler;

    @Mock
    Time time;

    @Mock
    Bot bot;

    @Mock
    ScheduledFuture<?>notification;

    User user;

    /**
     * Инициализация переменных database, parser, user перед каждым тестом
     * @throws SQLException не бросается
     * @throws IOException не бросается
     * @throws NoSuchElementException не бросается
     */
    @BeforeEach
    public void MockitoInit() throws SQLException, IOException, NoSuchElementException{
        List<String> schedule = Arrays.asList("8:00 Матан", "-", "10:00 Алгебра и геометрия");
        List<List<String>> weekSchedule = Arrays.asList(schedule);
        String id = "228";
        String group = "МЕН-220201";
        int shiftDay = 1;                       //Monday

        user = new User(database, id, parser, time, bot, scheduler);

        Mockito.when(database.getUsersGroup(id)).thenReturn(group);
        Mockito.when(database.getNotificationShift(id)).thenReturn(10);    //10 минут
        Mockito.when(database.getSchedule(id, shiftDay)).thenReturn(schedule);
        Mockito.when(database.tableIsExist(group)).thenReturn(true);
        Mockito.when(database.getUserIdNotification()).thenReturn(Arrays.asList(id));

        Mockito.when(time.getTime()).thenReturn(6 * 60);                    //6:00 сейчас
        Mockito.when(time.getTime(schedule.get(0))).thenReturn(8 * 60);     //8:00
        Mockito.when(time.getTime(schedule.get(1))).thenReturn(10 * 60);    //10:00
        Mockito.when(time.getLocalDate("1.12")).thenReturn(LocalDate.of(0, 12, 1));    //1.12 0 (год не важен)        user = new User(database, "228", parser, time, bot, scheduler);
        Mockito.when(time.getShift(Mockito.any(LocalDate.class))).thenReturn(shiftDay);
        Mockito.when(time.utilTomorrow()).thenReturn((long) (18 * 60));                  //18 часов

        Mockito.when(parser.parse(time, group)).thenReturn(weekSchedule);
    }


    /**
     * Проверка корректности включения уведомлений
     */
    @Test
    public void notificationOnDefaultTest(){
        user.processMessage("/notification_on");
        Mockito.verify(scheduler).schedule(Mockito.any(Runnable.class), Mockito.eq((long) (2 * 60 - 10)), Mockito.eq(TimeUnit.MINUTES));
    }

}
