package botLogic.utils;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс обработчика времени
 */
public class Calendar implements Time{

    /**
     * Вычисляет общее время (в минутах)
     */
    public int getSecondsOfDay(){
        return LocalTime.now().toSecondOfDay();
    }

    /**
     * Извлекает из пары общее время (в минутах)
     */
    public int getSecondsOfDay(String lesson) throws DateTimeException {
        Pattern pattern = Pattern.compile("(\\d{1,2}):(\\d{2})");

        Matcher matcher = pattern.matcher(lesson);
        if(!matcher.find()) throw new DateTimeException("не удалось считать расписание");

        int hour = Integer.parseInt(matcher.group(1));
        int minute = Integer.parseInt(matcher.group(2));

        return LocalTime.of(hour, minute).toSecondOfDay();
    }

    /**
     * Извлекает из date localDate
     */
    public LocalDate getLocalDate(String date) throws DateTimeException{
        Pattern pattern = Pattern.compile("^(\\d{1,2}).(\\d{2})$");

        Matcher matcher = pattern.matcher(date);
        if(!matcher.find()) throw new DateTimeException("дата введена некорректно");

        int day = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        return LocalDate.of(0, month, day);
    }

    /**
     * Вычисляет сколько осталось до завтра (в минутах)
     */
    public int getSecondsUtilTomorrow(){
        LocalTime tomorrowTime = LocalTime.MAX;
        LocalTime currentTime = LocalTime.now();
        return (int)currentTime.until(tomorrowTime, ChronoUnit.SECONDS);
    }

    /**
     * Возвращает сколько дней прошло с прошлой чётной недели первого дня относительно даты
     * @param date дата
     * @return смещенная дата
     */
    public int getShift(final LocalDate date){
        LocalDate shiftDate = LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        int weekOfYear = shiftDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);

        if (shiftDate.getDayOfWeek() == DayOfWeek.SUNDAY) //неделя в России начинается не с воскресенья
            weekOfYear -= 1;

        if (weekOfYear % 2 == 0) shiftDate = shiftDate.with(ChronoField.DAY_OF_WEEK, 1);
        else shiftDate = shiftDate.minusWeeks(1).with(ChronoField.DAY_OF_WEEK, 1);

        return (int)shiftDate.until(date, ChronoUnit.DAYS) + 1;
    }
}
