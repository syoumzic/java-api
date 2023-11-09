package botLogic;

import botLogic.dataBase.Data;
import botLogic.dataBase.Database;
import botLogic.parser.Parser;
import botLogic.parser.WebParser;
import botLogic.utils.Calendar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Проверка обработки команд
 */
public class CommandTest {
    @Mock
    Data database;
    Parser parser;
    User user;
    List<String> schedule = Arrays.asList("8:00 Матан", "-", "10:00 Алгебра и геометрия");
    List<List<String>> weekSchedule;

    /**
     * Инициализация переменных database, parser, user перед каждым тестом
     * @throws SQLException не бросается
     */
    @Before
    public void MockitoInit() throws SQLException {
        database = Mockito.mock(Database.class);
        parser = Mockito.mock(WebParser.class);

        Mockito.when(database.getUsersGroup(Mockito.anyString())).thenReturn("МЕН-220201");

        Mockito.when(database.getSchedule(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(schedule);
        user = new User(database, "228", parser, new Calendar());
    }

    /**
     * Проверка команды /schedule
     */
    @Test
    public void processMessage_schedule_verfy(){
        user.processMessage("/schedule");
        String answer = user.processMessage("1.12");

        Assert.assertEquals("8:00 Матан\n" +
                                    "-\n" +
                                    "10:00 Алгебра и геометрия\n", answer);
    }

    /**
     * Проверка команды /schedule с не корректными данными
     */
    @Test
    public void processMessage_schedule_DataTimeParseException(){
        user.processMessage("/schedule");
        String answer = user.processMessage("1.13");

        Assert.assertEquals("Дата введена некорректно", answer);
    }

    /**
     * Проверка команды /schedule с ещё не введённым номером группы
     * @throws SQLException не бросается
     */
    @Test
    public void processMessage_schedule_withNoGroup() throws SQLException{
        Mockito.when(database.getUsersGroup(Mockito.anyString())).thenThrow(SQLException.class);

        user.processMessage("/schedule");
        String answer = user.processMessage("1.12");

        Assert.assertEquals("Для начала укажите свою группу", answer);
    }

    /**
     * Проверка команды /change_group на запись в базу данных
     * @throws SQLException не бросается
     */
    @Test
    public void processMessage_changeGroup() throws SQLException{
        user.processMessage("/change_group");
        String answer = user.processMessage("МЕН-220201");

        Mockito.verify(database).addUserGroup(Mockito.anyString(), Mockito.anyString());

        Assert.assertEquals("Группа успешно обновлена!", answer);
    }

    /**
     * Проверка команды /change_group с некорректной группой
     */
    @Test
    public void processMessage_changeGroup_incorrectGroup(){
        user.processMessage("/change_group");
        String answer = user.processMessage("-220201");

        Assert.assertEquals("Группа введена некорректно", answer);
    }

    /**
     * Проверка команды /change_group с группой, у которой нечитабельное расписание
     * @throws IOException обрабатывается
     * @throws SQLException не бросается
     */
    @Test
    public void processMessage_changeGroup_IOException() throws IOException, SQLException{
        Mockito.when(parser.parse(Mockito.any(), Mockito.anyString())).thenThrow(IOException.class);
        user.processMessage("/change_group");
        String answer = user.processMessage("МЕН-220201");

        Mockito.verify(database, Mockito.never()).setSchedule(Mockito.anyString(), Mockito.any());
    }

    /**
     * Проверка команды /change_group с группы, которой нет на сайте UrFU
     * @throws IOException не бросается
     * @throws SQLException не бросается
     */
    @Test
    public void processMessage_changeGroup_NoSuchElementException() throws IOException, SQLException{
        Mockito.when(parser.parse(Mockito.any(), Mockito.anyString())).thenThrow(NoSuchElementException.class);

        user.processMessage("/change_group");
        String answer = user.processMessage("МЕН-220201");

        Mockito.verify(database, Mockito.never()).setSchedule(Mockito.anyString(), Mockito.any());
    }

    /**
     * Проверка команды /change_schedule
     * @throws SQLException не бросается
     */
    @Test
    public void processMessage_changeSchedule() throws SQLException{
        user.processMessage("/change_schedule");
        user.processMessage("1.12");
        user.processMessage("8:00 Матан\n" +
                            "10:40 Алгем");

        Mockito.verify(database).setCastomSchedule(Mockito.anyString(), Mockito.any(), Mockito.anyInt());
    }

    /**
     * Проверка команды /next_lesson
     * @throws SQLException не бросается
     */
    @Test
    public void processMessage_nextLesson() throws SQLException{
        user.processMessage("/next_lesson");

        Mockito.verify(database).getNextLesson(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
    }
}
