package botLogic.utils;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Locale;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс обработчика времени
 */
public class Calendar implements Time{

    /**
     * Вычисляет общее время (в минутах)
     */
    public int getTime(){
        LocalTime currentTime = LocalTime.now();
        return currentTime.getHour() * 60 + currentTime.getMinute();
    }

    /**
     * Извлекает из пары общее время (в минутах)
     */
    public int getTime(String lesson) throws DateTimeException {
        Pattern pattern = Pattern.compile("(\\d{1,2}):(\\d{2})");

        Matcher matcher = pattern.matcher(lesson);
        if(!matcher.find()) throw new DateTimeException("не удалось считать расписание");

        int hour = Integer.parseInt(matcher.group(1));
        int minute = Integer.parseInt(matcher.group(2));

        LocalTime.of(hour, minute);          //дополнительная проверка на валидность даты
        return hour * 60 + minute;
    }

    /**
     * Извлекает из lesson localDate
     */
    public LocalDate getLocalDate(String lesson) throws DateTimeException{
        Pattern pattern = Pattern.compile("(\\d{1,2}).(\\d{2})");

        Matcher matcher = pattern.matcher(lesson);
        if(!matcher.find()) throw new DateTimeException("дата введена некорректно");

        int day = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        return LocalDate.of(0, month, day);
    }

    /**
     * Вычисляет сколько осталось до завтра (в минутах)
     */
    public long utilTomorrow(){
        LocalTime tomorrowTime = LocalTime.MAX;
        LocalTime currentTime = LocalTime.now();
        return currentTime.until(tomorrowTime, ChronoUnit.MINUTES);
    }

    /**
     * Возвращает сколько дней прошло с прошлой чётной недели первого дня относительно даты
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
