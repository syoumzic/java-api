package botLogic.parser;

import botLogic.utils.Time;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Интерфейс для управления считывателя расписания
 */
public interface Parser{
    /**
     * Возвращает расписание по номеру группы
     * @param group номер группы
     * @return расписание на 14 дней
     * @throws IOException ошибка считывания расписания
     * @throws NoSuchElementException группа не найдена
     */
    List<List<String>> parse(Time time, String group) throws IOException, NoSuchElementException;
}