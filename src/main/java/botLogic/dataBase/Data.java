package botLogic.dataBase;

import java.sql.SQLException;
import java.util.List;

/**
 * Интерфейс, представляющий собой возможность управления данными
 */
public interface Data {
    /**
     * Метод для считывания расписания из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param day Номер дня недели от 1 до 14(Четная и Нечетная недели)
     * @return Возвращает расписание на день по id пользователя.
     * @throws SQLException Ошибка существования таблицы в базе данных.
     */
    List<String> getSchedule(String id, int day) throws SQLException;

    /**
     * Метод для записи расписания в базу данных.
     * @param group Номер группы пользователя.
     * @param schedule Расписание для записи в базу данных.
     */
    void setSchedule(String group, List<List<String>> schedule) throws SQLException;

    /**
     * Метод для записи в базу данных индивидуального расписания пользователя.
     * @param id Идентификатор пользователя в базе данных.
     * @param schedule Индивидуальное расписание для записи.
     * @param day Номер дня недели, на который сохраняется расписание.
     */
    void setCastomSchedule(String id, List<String> schedule, int day) throws SQLException;

    /**
     * Метод позволяет узнать следующую пару на текущий момент.
     * @param id Идентификатор пользователя в базе данных.
     * @param day Номер текущего дня недели от 1 до 14.
     * @param current_time Текущее время в минутах с начала дня
     * @return Возвращает следующую пару.
     */
    String getNextLesson (String id, int day, int current_time) throws SQLException;

    /**
     * Метод добавляющий пользователя в базу данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param group Номер группы пользователя.
     */
    void addUserGroup(String id, String group) throws SQLException;

    /**
     * Метод для записи настройки времени в базу данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param time Время в минутах для сохранения.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    void setUserTime(String id, int time) throws SQLException;

    /**
     * Метод для получения времени в минутах пользователя из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @return Возвращает число - количество минут.
     */
    Integer getUsersTime(String id) throws SQLException;

    /**
     * Метод для смены статуса уведомлений пользователя в базе данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param status Принимает значения: 1 - уведомления включены / 0 - выключены.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    void setStatusNotifications(String id, int status) throws SQLException;

    /**
     * Метод для получения состояния уведомления из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @return Возвращает состояние: 1 - уведомления включены / 0 - выключены.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    Integer getStatusNotifications(String id) throws SQLException;

    /**
     * Метод для получения номера группы пользователя из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @return Возвращает номер группы.
     */
    String getUsersGroup(String id) throws SQLException;

    /**
     * Проверка существования таблицы в базе данных.
     * @param name_table Имя таблицы в базе данных.
     * @return Возвращает, существует ли таблица в базе данных.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    Boolean tableIsExist(String name_table) throws SQLException;

    /**
     * Метод для удаления индивидуального расписания из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param day Номер дня от 0 до 14, где 0 - полное удаление.
     */
    void deleteSchedule(String id, int day) throws SQLException;

}