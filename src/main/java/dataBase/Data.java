package dataBase;

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
    void setNotificationShift(String id, int time) throws SQLException;

    /**
     * Метод для получения времени в минутах пользователя из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @return Возвращает число - количество минут.
     */
    Integer getNotificationShift(String id) throws SQLException;

    /**
     * Метод для смены статуса уведомлений пользователя в базе данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param status Принимает значения: 1 - уведомления включены / 0 - выключены.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    void setStatusNotifications(String id, int status) throws SQLException;

    /**
     * Метод для получения состояния уведомления у пользователя из базы данных.
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
     * Метод для получения списка id пользователей, с включёнными уведомлениями.
     * @return Возвращает список id пользователей.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    List<String> getUserIdNotification() throws  SQLException;

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

    /**
     * Метод для получения списка дедлайнов пользователя.
     * @param id Пользователя.
     * @param date Дата, на которую нужно получить дедлайн, в формате d.mm.
     * @return Возвращает список всех дедлайнов на день.
     * @throws SQLException Отсутствует таблица код ошибки 1146.
     */
    List<String> getDeadlines(String id, String date) throws SQLException;

    /**
     * Метод для перезаписи дедлайнов на день.
     * @param id Пользователя.
     * @param newDeadlines Список новых дедлайнов для перезаписи.
     * @param date Дата, на которую нужно получить дедлайн, в формате d.mm.
     * @throws SQLException Ошибка отсутствия записей на этот день.
     */
    void editDeadlines(String id, List<String> newDeadlines, String date) throws SQLException;

    /**
     * Метод для записи дедлайнов на определённую дату.
     * @param id Пользователя.
     * @param deadlines Список дедлайнов для записи.
     * @param date Дата, на которую нужно получить дедлайн, в формате d.mm.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    void setDeadlines(String id, List<String>deadlines, String date) throws SQLException;

    /**
     * Метод для получения всех id пользователей, у которых есть таблица дедлайнов.
     * @return Возвращает id пользователей.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    List<String> getUsersIdDeadline() throws  SQLException;

    /**
     * Метод для установки времени, за которое нужно предупредить о дедлайне.
     * @param id Пользователя.
     * @param hours Количество часов.
     * @throws SQLException Ошибка записи в базу данных.
     */
    void setDeadlineNotificationShift(String id, int hours) throws SQLException;

    /**
     * Метод для получения времени, за которое нужно предупредить о дедлайне.
     * @param id Пользователя.
     * @return Количество часов.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    int getDeadlineNotificationShift(String id) throws SQLException;
}