package botLogic;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class Calendar {

    /**
     * преобразует часы и минуты в числоо
     * @return вычисляет сколько минут прошло с начала дня
     */
    public int getMinute(){
        Date date = new Date();  // current time
        return date.getHours() * 60 + date.getMinutes();
    }

    /**
     * переводит строку в дату
     * @param date строка
     * @throws DateTimeParseException если перевести в формат невозможно
     * @return возвращает дату
     */
    public LocalDate getLocalDate(String date) throws DateTimeParseException{
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("d.MM")
                .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
                .toFormatter(Locale.US);

        return LocalDate.parse(date, formatter);
    }

    /**
     * возвращает сколько дней прошло с прошлой чётной недели первого дня относительно даты
     * @param date дата
     * @return смещенная дата
     */
    public int getShift(LocalDate date){
        LocalDate shiftDate = LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        int weekOfYear = shiftDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);

        if (shiftDate.getDayOfWeek() == DayOfWeek.SUNDAY) //неделя в России начинается не с воскресенья
            weekOfYear -= 1;

        if (weekOfYear % 2 == 0) shiftDate = shiftDate.with(ChronoField.DAY_OF_WEEK, 1);
        else shiftDate = shiftDate.minusWeeks(1).with(ChronoField.DAY_OF_WEEK, 1);

        return (int)shiftDate.until(date, ChronoUnit.DAYS) + 1;
    }
}
