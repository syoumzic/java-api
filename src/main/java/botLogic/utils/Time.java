package botLogic.utils;

import java.time.DateTimeException;
import java.time.LocalDate;

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
    int getTime(String lesson) throws DateTimeException;

    /**
     * Извлекает из localDate
     */
    LocalDate getLocalDate(String lesson) throws DateTimeException;

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
