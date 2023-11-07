package botLogic;

import botLogic.utils.Calendar;
import botLogic.utils.Time;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CalendarTest {

    /**
     * проверяет корректность преобразования даты
     */
    @Test
    public void checkCorrectDate() {
        Time calendar = new Calendar();

        Assertions.assertEquals(LocalDate.of(2023, 11, 3), calendar.getLocalDate("3.11"));
        Assertions.assertEquals(LocalDate.of(2023, 11, 2), calendar.getLocalDate("2.11"));
        Assertions.assertEquals(LocalDate.of(2023, 10, 31), calendar.getLocalDate("31.10"));
        Assertions.assertEquals(LocalDate.of(2023, 1, 1), calendar.getLocalDate("1.01"));

        Assertions.assertThrows(DateTimeParseException.class, () -> calendar.getLocalDate("3.11.2023"));
        Assertions.assertThrows(DateTimeParseException.class, () -> calendar.getLocalDate("2023.11.01"));
        Assertions.assertThrows(DateTimeParseException.class, () -> calendar.getLocalDate("3 ноября"));
        Assertions.assertThrows(DateTimeParseException.class, () -> calendar.getLocalDate("1.38"));
    }
}
