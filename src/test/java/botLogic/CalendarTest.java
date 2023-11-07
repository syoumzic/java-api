package botLogic;

import org.junit.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.assertThrows;

public class CalendarTest {

    @Test
    public void checkCorrectDate() {
        Calendar calendar = new Calendar();

        Assert.assertEquals(LocalDate.of(2023, 11, 3), calendar.getLocalDate("3.11"));
        Assert.assertEquals(LocalDate.of(2023, 11, 2), calendar.getLocalDate("2.11"));
        Assert.assertEquals(LocalDate.of(2023, 10, 31), calendar.getLocalDate("31.10"));
        Assert.assertEquals(LocalDate.of(2023, 1, 1), calendar.getLocalDate("1.01"));

        assertThrows(DateTimeParseException.class, () -> calendar.getLocalDate("3.11.2023"));
        assertThrows(DateTimeParseException.class, () -> calendar.getLocalDate("2023.11.01"));
        assertThrows(DateTimeParseException.class, () -> calendar.getLocalDate("3 ноября"));
        assertThrows(DateTimeParseException.class, () -> calendar.getLocalDate("1.38"));
    }
}
