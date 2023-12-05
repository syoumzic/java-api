package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Проверяет работу обработчика времени
 */
public class CalendarTest {

    /**
     * Проверяет корректность преобразования даты из String в LocalDate
     */
    @Test
    public void toStringTest() {
        Time time = new Calendar();

        int year = LocalDate.now().getYear();

        Assertions.assertEquals(LocalDate.of(year, 11, 3), time.getLocalDate("3.11"));
        Assertions.assertEquals(LocalDate.of(year, 11, 2), time.getLocalDate("2.11"));
        Assertions.assertEquals(LocalDate.of(year, 10, 31), time.getLocalDate("31.10"));
        Assertions.assertEquals(LocalDate.of(year, 1, 1), time.getLocalDate("1.01"));

        Assertions.assertThrows(DateTimeException.class, () -> time.getLocalDate("3.11.2023"));
        Assertions.assertThrows(DateTimeException.class, () -> time.getLocalDate("2023.11.01"));
        Assertions.assertThrows(DateTimeException.class, () -> time.getLocalDate("3 ноября"));
        Assertions.assertThrows(DateTimeException.class, () -> time.getLocalDate("1.38"));
    }

    /**
     * Проверяет корректность преобразования даты из LocalDate в String
     */
    @Test
    public void toLocalDateTest(){
        Time time = new Calendar();

        Assertions.assertEquals("1.10.2023", time.getDateString(LocalDate.of(2023, 10, 1)));
        Assertions.assertEquals("10.1.2023", time.getDateString(LocalDate.of(2023, 1, 10)));
    }
}
