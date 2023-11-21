package botLogic.utils;

import javax.validation.constraints.NotNull;
import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Интерфейс для управления обработчиком времени
 */
public interface Time {
    /**
     * Вычисляет общее время
     */
    int getSecondsOfDay();

    /**
     * Извлекает из пары общее время
     */
    int getSecondsOfDay(String lesson) throws DateTimeException;

    /**
     * Извлекает из localDate
     */
    LocalDate getLocalDate(String date) throws DateTimeException;

    /**
     * Вычисляет сколько осталось до завтра
     */
    int getSecondsUtilTomorrow();

    /**
     * Возвращает сколько дней прошло с прошлой чётной недели первого дня относительно даты
     * @param date дата
     * @return смещенная дата
     */
    int getShift(LocalDate date);
}
