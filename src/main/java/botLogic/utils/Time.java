package botLogic.utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Интерфейс для управления обработчиком времени
 */
public interface Time {
    /**
     * Вычисляет общее время (в минутах)
     */
    int getTime();

    /**
     * Извлекает из пары общее время (в минутах)
     */
    int getTime(String lesson) throws IOException;

    /**
     * Извлекает из localDate
     */
    LocalDate getLocalDate(String lesson) throws IOException;

    /**
     * Вычисляет сколько осталось до завтра (в минутах)
     */
    long utilTomorrow();

    /**
     * Возвращает сколько дней прошло с прошлой чётной недели первого дня относительно даты
     * @param date дата
     * @return смещенная дата
     */
    int getShift(LocalDate date);
}
