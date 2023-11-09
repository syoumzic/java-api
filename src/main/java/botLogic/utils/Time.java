package botLogic.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Интерфейс для управления обработчиком времени
 */
public interface Time {
    /**
     * Преобразует часы и минуты в число
     * @return вычисляет сколько минут прошло с начала дня
     */
    int getMinute();

    /**
     * Переводит строку в дату
     * @param date строка
     * @throws DateTimeParseException если перевести в формат невозможно
     * @return возвращает дату
     */
    LocalDate getLocalDate(String date) throws DateTimeParseException;

    /**
     * Возвращает сколько дней прошло с прошлой чётной недели первого дня относительно даты
     * @param date дата
     * @return смещенная дата
     */
    int getShift(LocalDate date);
}
