package botLogic;

import JavaBots.Bot;
import botLogic.dataBase.Data;
import botLogic.parser.Parser;
import botLogic.utils.Calendar;
import botLogic.utils.Time;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Проверка обработки команд
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommandTest {
    @Mock
    Data database;

    @Mock
    Parser parser;

    @Mock
    ScheduledExecutorService scheduler;

    @Mock
    Bot bot;

    MockedStatic<Clock> clockMock;

    Time time;
    User user;
    Logic logic;

    @Captor
    ArgumentCaptor<Runnable> runnableCaptor;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    final LocalDate localDate = LocalDate.of(2023, 11, 27);
    final String dateString = "%s.%s".formatted(localDate.getDayOfMonth(), localDate.getMonthValue());
    final LocalTime localTime = LocalTime.of(6, 0);
    final List<String> schedule = Arrays.asList("8:00 Матан", "10:00 Алгем", "12:00 Дискретка");
    final String id = "T228";
    final String group = "МЕН-220201";

    /**
     * Инициализация переменных database, parser, user перед каждым тестом
     * @throws SQLException не бросается
     * @throws IOException не бросается
     * @throws NoSuchElementException не бросается
     */
    @BeforeEach
    public void MockitoInit() throws SQLException, IOException, NoSuchElementException{
        time = new Calendar();
        user = new User(database, id, parser, time, bot, scheduler);
        logic = new Logic(database, parser, time, scheduler);

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Instant fixedInstant = Instant.ofEpochMilli(instant.toEpochMilli());    //в миллисекундах
        Clock fixedClock = Clock.fixed(fixedInstant, zoneId);

        clockMock = Mockito.mockStatic(Clock.class);
        clockMock.when(Clock::systemDefaultZone).thenReturn(fixedClock);
        Assertions.assertEquals(localTime, LocalTime.now());

//        Mockito.when(time.getSecondsOfDay()).thenReturn(6 * 60 * 60);                         //6:00
//        Mockito.when(time.getSecondsUtilTomorrow()).thenReturn(18 * 60 * 60);                 //18 часов
//        Mockito.when(time.getSecondsOfDay(schedule.get(0))).thenReturn(8 * 60 * 60);          //8:00
//        Mockito.when(time.getSecondsOfDay(schedule.get(2))).thenReturn(10 * 60 * 60);         //10:00
//        Mockito.when(time.getLocalDate(date)).thenReturn(LocalDate.of(2023, 12, 1));    //1.12 (год не важен)
//        Mockito.when(time.getShift(Mockito.any(LocalDate.class))).thenReturn(shiftDay);                //Понедельник
//        Mockito.when(time.getShift()).thenReturn(shiftDay);                //Понедельник

        Mockito.when(database.getUsersGroup(id)).thenReturn(group);
        Mockito.when(database.getNotificationShift(id)).thenReturn(10);                      //10 минут
        Mockito.when(database.getSchedule(id, time.getShift())).thenReturn(schedule);
        Mockito.when(database.tableIsExist(group)).thenReturn(true);
        Mockito.when(database.getUserIdNotification()).thenReturn(List.of(id));

        Mockito.when(parser.parse(time, group)).thenReturn(List.of(schedule));
    }

    /**
     * Удаление mock объектов перед стартов нового теста
     */
    @AfterEach
    public void MockitoAfter(){
        clockMock.close();
    }

    /**
     * Проверка команды /schedule
     */
    @Test
    public void getScheduleVerifyTest(){
            user.processMessage("/schedule");
            String answer = user.processMessage(dateString);

            String stringSchedule = String.join("\n", schedule) + "\n";
            Assertions.assertEquals(stringSchedule, answer);
    }

    /**
     * Проверка команды /schedule с ещё не введённым номером группы
     */
    @Test
    public void getScheduleWithNoGroupTest() throws SQLException{
        Mockito.when(database.getUsersGroup(id)).thenThrow(SQLException.class);

        user.processMessage("/schedule");
        String answer = user.processMessage(dateString);

        Assertions.assertEquals("Для начала укажите свою группу", answer);
    }

    /**
     * Проверка команды /change_group на запись в базу данных
     * @throws SQLException не бросается
     */
    @Test
    public void changeGroupVerifyTest() throws SQLException{
        user.processMessage("/change_group");
        String answer = user.processMessage(group);

        Mockito.verify(database).addUserGroup(id, group);
        Assertions.assertEquals("Группа успешно обновлена!", answer);
    }

    /**
     * Проверка команды /change_group с некорректной группой
     * @throws SQLException не вызывается
     */
    @Test
    public void changeGroupWithIncorrectArgumentTest() throws SQLException{
        String incorrectGroup = "-220201";
        user.processMessage("/change_group");
        String answer = user.processMessage(incorrectGroup);

        Mockito.verify(database, Mockito.never()).addUserGroup(id, incorrectGroup);
        Assertions.assertEquals("Группа введена некорректно", answer);
    }

    /**
     * Проверка команды /change_group с группой, у которой нечитабельное расписание
     * @throws IOException обрабатывается
     * @throws SQLException не бросается
     */
    @Test
    public void changeGroupUnreadScheduleTest() throws IOException, SQLException{
        Mockito.when(parser.parse(time, group)).thenThrow(IOException.class);
        Mockito.when(database.tableIsExist(group)).thenReturn(false);

        user.processMessage("/change_group");
        String answer = user.processMessage(group);

        Mockito.verify(parser).parse(time, group);
        Mockito.verify(database, Mockito.never()).setSchedule(Mockito.anyString(), Mockito.any());
        Assertions.assertEquals("Ошибка считывания расписания.", answer);
    }

    /**
     * Проверка команды /change_group с группы, которой нет на сайте UrFU
     * @throws IOException не бросается
     * @throws SQLException не бросается
     */
    @Test
    public void changeGroupNoSuchGroupTest() throws IOException, SQLException{
        Mockito.when(parser.parse(time, group)).thenThrow(NoSuchElementException.class);

        user.processMessage("/change_group");
        String answer = user.processMessage(group);

        Assertions.assertEquals("Не удалось найти группу с таким номером", answer);
        Mockito.verify(parser).parse(time, group);
        Mockito.verify(database, Mockito.never()).setSchedule(Mockito.anyString(), Mockito.any());
    }

    /**
     * Проверка команды /change_schedule
     * @throws SQLException не бросается
     */
    @Test
    public void changeScheduleVerifyTest() throws SQLException{
        List<String> customSchedule = List.of("8:00 Матан", "10:00 Алгем");
        String customScheduelString = String.join("\n", customSchedule);

        user.processMessage("/change_schedule");
        user.processMessage(dateString);
        String answer = user.processMessage(customScheduelString);

        Assertions.assertEquals("Расписание обновлено", answer);
        Mockito.verify(database).setCastomSchedule(id, customSchedule, time.getShift());
    }

    /**
     * Проверка команды /next_lesson
     * @throws SQLException не бросается
     */
    @Test
    public void nextLessonVerify() throws SQLException{
        String answer = user.processMessage("/next_lesson");

        Assertions.assertEquals(schedule.get(0), answer);
        Mockito.verify(database).getSchedule(id, time.getShift());
    }

    /**
     * Проверка корректности включения уведомлений
     */
    @Test
    public void setNotificationVerifyTest(){
        String answer = user.processMessage("/notification_on");

        Assertions.assertEquals("Уведомления включены", answer);
        Mockito.verify(scheduler, Mockito.times(group.length())).schedule(Mockito.any(Runnable.class), Mockito.anyLong(), Mockito.eq(TimeUnit.SECONDS));
        for(String lesson : schedule) {
            Mockito.verify(scheduler).schedule(runnableCaptor.capture(), Mockito.eq(time.getSecondsOfDay(lesson)), Mockito.eq(TimeUnit.SECONDS));
            runnableCaptor.getValue().run();
            Mockito.verify(bot).sendMessage(Long.parseLong(id.substring(1)), lesson);
        }
    }

    /**
     * Проверка корректности невключения при отсутствии группы
     * @throws SQLException обрабатывается
     */
    @Test
    public void setNotificationWithNoGroupTest() throws SQLException{
        Mockito.when(database.getUsersGroup(id)).thenThrow(SQLException.class);

        String answer = user.processMessage("/notification_on");
        Assertions.assertEquals("Для начала укажите группу", answer);
    }

    /**
     * Проверка установки времени уведомлений
     * @throws SQLException не обрабатывается
     */
    @Test
    public void settingsNotificationVerifyTest() throws SQLException{
        int timeShift = 50;

        user.processMessage("/notification_set");
        String answer = user.processMessage(Integer.toString(timeShift));

        Assertions.assertEquals("Время установлено", answer);
        Mockito.verify(database).setNotificationShift(id, timeShift);
    }

    /**
     * Проверка установки времени уведомлений с некорректным количеством минут
     * @throws SQLException не обрабатывается
     */
    @Test
    public void settingsNotificationIncorrectTest() throws SQLException{
        user.processMessage("/notification_set");
        String answer = user.processMessage("dkfjo");

        Assertions.assertEquals("Введено не время", answer);
        Mockito.verify(database, Mockito.never()).setNotificationShift(Mockito.eq(id), Mockito.anyInt());
    }

    /**
     * Проверка установки времени уведомлений с выходом за границы установленного времени
     * @throws SQLException не обрабатывается
     */
    @Test
    public void settingsNotificationOutOfRangeTest() throws SQLException{
        user.processMessage("/notification_set");
        String answer = user.processMessage("1021");

        Assertions.assertEquals("Ожидается число от 0 до 90", answer);
        Mockito.verify(database, Mockito.never()).setNotificationShift(Mockito.eq(id), Mockito.anyInt());
    }
}
