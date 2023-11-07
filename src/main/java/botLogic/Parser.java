package botLogic;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public interface Parser{
    /**
     * возвращает расписание по номеру группы
     * @param group номер группы
     * @return расписание на 14 дней
     * @throws IOException ошибка считывания расписания
     * @throws NoSuchElementException группа не найдена
     */
    List<List<String>> parse(String group) throws IOException, NoSuchElementException;
}