package botLogic.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public interface Time {
    int getMinute();
    LocalDate getLocalDate(String date) throws DateTimeParseException;
    int getShift(LocalDate date);
}
