package botLogic;

import botLogic.utils.Calendar;
import botLogic.utils.Time;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Проверяет работу обработчика времени
 */
public class CalendarTest {

    /**
     * Проверяет корректность преобразования даты из String в LocalDate
     */
    @Test
    public void checkCorrectDate() {
        Time time = new Calendar();
        Assertions.assertEquals(LocalDate.of(0, 11, 3), time.getLocalDate("3.11"));
        Assertions.assertEquals(LocalDate.of(0, 11, 2), time.getLocalDate("2.11"));
        Assertions.assertEquals(LocalDate.of(0, 10, 31), time.getLocalDate("31.10"));
        Assertions.assertEquals(LocalDate.of(0, 1, 1), time.getLocalDate("1.01"));

        Assertions.assertThrows(DateTimeException.class, () -> time.getLocalDate("3.11.2023"));
        Assertions.assertThrows(DateTimeException.class, () -> time.getLocalDate("2023.11.01"));
        Assertions.assertThrows(DateTimeException.class, () -> time.getLocalDate("3 ноября"));
        Assertions.assertThrows(DateTimeException.class, () -> time.getLocalDate("1.38"));
    }
}
