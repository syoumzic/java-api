package botLogic.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс представляющий собой данные
 */
public class Database implements Data {
    private final String url = System.getenv("URL");
    private final String user = System.getenv("NAMEUSER");
    private final String password = System.getenv("PASSUSER");

    // JDBC variables for opening and managing connection
    private Connection connect;
    private Statement state;
    private ResultSet result;

    /**
     * Метод для считывания расписания из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param day Номер дня недели от 1 до 14(Четная и Нечетная недели)
     * @return Возвращает расписание на день по id пользователя.
     * @throws SQLException Ошибка существования таблицы в базе данных.
     */
    public List<String> getSchedule(String id, int day) throws SQLException{
        String group = null;
        String lesson = null;
        List<String> schedule = null;
        boolean flag = false;
        try {
            return getCastomSchedule(id, day);
        } catch (SQLException ex) {
            flag = true;
        }
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        result = state.executeQuery(String.format("Select `useIndiv` from `users` where `id` = '%s'", id));
        if (result.next() && (result.getInt(1) == 0 || flag)) {
            result = state.executeQuery(String.format("SELECT `group` FROM `users` WHERE id='%s'", id));
            if (result.next()) group = result.getString(1);
            result = state.executeQuery(String.format("SELECT `%s` FROM `%s`", day, group.toLowerCase()));
            schedule = new ArrayList<>();
            while (result.next()) {
                lesson = result.getString(1);
                if (Objects.equals(lesson, "end")) break;
                schedule.add(lesson);
            }

        }
        connect.close();
        state.close();
        result.close();
        return schedule;
    }

    /**
     * Метод для записи расписания в базу данных.
     * @param group Номер группы пользователя.
     * @param schedule Расписание для записи в базу данных.
     */
    public void setSchedule(String group, List<List<String>> schedule) throws SQLException{
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        try {
            group = group.toLowerCase();
            if (state.executeQuery(String.format("show tables like '%s'", group)).next()) {
                dropTable(group);
            }
            StringBuilder query = new StringBuilder(String.format("CREATE TABLE `%s` (`id` INT NOT NULL AUTO_INCREMENT,", group));
            for (int i = 0; i < 14; i++) {
                query.append(String.format(" `%s` VARCHAR(300) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL DEFAULT '-',", (i + 1)));
            }
            query.append(" primary key (id)) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8");
            state.executeUpdate(query.toString());
            Iterator<String> iter;
            int max_iter = 0;
            int index;
            for (int i = 0; i < 14; i++) {
                iter = schedule.get(i).iterator();
                index = 0;
                while (iter.hasNext() && index < max_iter) {
                    index++;
                    state.executeUpdate(String.format("UPDATE `%s` SET `%s` = '%s' WHERE `id` = '%s'", group, i + 1, iter.next(), index));
                }
                while (iter.hasNext()) {
                    state.executeUpdate(String.format("INSERT INTO `%s` (`%s`) VALUES ('%s')", group, i + 1, iter.next()));
                }
                max_iter = Integer.max(max_iter, schedule.get(i).size());
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        } finally {
            connect.close();
            state.close();
        }
    }

    /**
     * Метод для записи в базу данных индивидуального расписания пользователя.
     * @param id Идентификатор пользователя в базе данных.
     * @param schedule Индивидуальное расписание для записи.
     * @param day Номер дня недели, на который сохраняется расписание.
     */
    public void setCastomSchedule(String id, List<String> schedule, int day) throws SQLException{
        boolean flag = tableIsExist(id);
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        try {
            if (!flag) {
                state.executeUpdate(String.format("Create table `%s`(`id` int primary key not null auto_increment)", id));
                switchUserStatus(id);
                connect = DriverManager.getConnection(url, user, password);
                state = connect.createStatement();
            }
            if (!state.executeQuery(String.format("show columns from `%s` like '%s'", id, day)).next())
                state.executeUpdate(String.format("Alter table `%s` Add column `%s` varchar(64) not null default '-'", id, day));
            result = state.executeQuery(String.format("select count(`id`) from `%s`", id));
            int size = 0;
            if (result.next()) size = result.getInt(1);
            int i = 0;
            for (String s : schedule) {
                if (i < size) {
                    state.executeUpdate(String.format("update `%s` set `%s` = '%s' where `id` = '%s'", id, day, s, i + 1));
                } else {
                    state.executeUpdate(String.format("insert into `%s` (`%s`) values ('%s')", id, day, s));
                }
                i++;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        }
        finally {
            connect.close();
            state.close();
        }
    }

    /**
     * Метод позволяет узнать следующую пару на текущий момент.
     * @param id Идентификатор пользователя в базе данных.
     * @param day Номер текущего дня недели от 1 до 14.
     * @param current_time Текущее время в минутах с начала дня
     * @return Возвращает следующую пару.
     */
    public String getNextLesson (String id, int day, int current_time) throws SQLException{
        String lesson = null;
        Pattern pattern = Pattern.compile("(\\d{1,2}):(\\d\\d)");
        Matcher matcher;
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        try {
            List<String> schedule = getSchedule(id, day);
            for (String less : schedule){
                matcher = pattern.matcher(less);
                matcher.find();
                if (!less.equals("-") && current_time <= Integer.parseInt(matcher.group(1)) * 60
                        + Integer.parseInt(matcher.group(2))) {
                    return less;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        } finally {
            connect.close();
            state.close();
        }
        return "Сегодня у вас больше нет пар";
    }

    /**
     * Метод добавляющий пользователя в базу данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param group Номер группы пользователя.
     */
    public void addUserGroup(String id, String group) throws SQLException{
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        try {
            if (state.executeQuery(String.format("Select `id` from `users` where `id` = '%s'", id)).next()){
                state.executeUpdate(String.format("UPDATE `users` SET `group` = '%s' WHERE `id` = '%s'", group, id));
            }
            else {
                state.executeUpdate(String.format("INSERT INTO `users` (`id`, `group`) VALUES ('%s', '%s')", id, group));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        } finally {
            connect.close();
            state.close();
        }
    }

    /**
     * Метод для записи настройки времени в базу данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param time Время в минутах для сохранения.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    public void setNotificationShift(String id, int time) throws SQLException{
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        try{
            state.executeUpdate(String.format("Update `users` set `time` = '%d' where `id` = '%s'", time, id));
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        } finally {
            connect.close();
            state.close();
        }
    }

    /**
     * Метод для получения времени в минутах пользователя из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @return Возвращает число - количество минут.
     */
    public Integer getNotificationShift(String id) throws SQLException{
        int time = 10;
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        try{
            result = state.executeQuery(String.format("SELECT `time` FROM `users` WHERE `id` = '%s'", id));
            if (result.next()) time = result.getInt(1);
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        } finally {
            connect.close();
            state.close();
        }
        return time;
    }

    /**
     * Метод для смены статуса уведомлений пользователя в базе данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param status Принимает значения: 1 - уведомления включены / 0 - выключены.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    public void setStatusNotifications(String id, int status) throws SQLException{
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        try{
            state.executeUpdate(String.format("Update `users` set `notification` = '%d' where `id` = '%s'", status, id));
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        } finally {
            connect.close();
            state.close();
        }

    }

    /**
     * Метод для получения состояния уведомления у пользователя из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @return Возвращает состояние: 1 - уведомления включены / 0 - выключены.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    public Integer getStatusNotifications(String id) throws SQLException{
        int status = 0;
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        result = state.executeQuery(String.format("SELECT `notification` FROM `users` WHERE `id` = '%s'", id));
        if (result.next()) status = result.getInt(1);
        connect.close();
        state.close();
        return status;
    }

    /**
     * Метод для получения номера группы пользователя из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @return Возвращает номер группы.
     * @throws SQLException Такого пользователя нет.
     */
    public String getUsersGroup(String id) throws SQLException{
        String group = null;
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        result = state.executeQuery(String.format("SELECT `group` FROM `users` WHERE `id` = '%s'", id));
        if (result.next()) group = result.getString(1);
        if (group == null) throw new SQLException();

        connect.close();
        state.close();

        return group;
    }

    /**
     * Метод для получения списка id пользователей, с включёнными уведомлениями.
     * @return Возвращает список id пользователей.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    public List<String> getUserIdNotification() throws  SQLException{
        List<String> usersId = new ArrayList<>();
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        result = state.executeQuery("SELECT `id` FROM `users` WHERE `notification` = 1");
        while (result.next()) {
            usersId.add(result.getString(1));
        }
        connect.close();
        state.close();

        return usersId;
    }

    /**
     * Метод для получения всех id пользователей, у которых есть таблица дедлайнов.
     * @return Возвращает id пользователей.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    public List<String> getUsersIdDeadline() throws  SQLException{
        List<String> usersId = new ArrayList<>();
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        result = state.executeQuery("select `id` from `users` where `existDL`='1'");
        while (result.next()) {
            usersId.add(result.getString(1));
        }
        connect.close();
        state.close();

        return usersId;
    }

    /**
     * Проверка существования таблицы в базе данных.
     * @param name_table Имя таблицы в базе данных.
     * @return Возвращает, существует ли таблица в базе данных.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    public Boolean tableIsExist(String name_table) throws SQLException{
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        try {
            return state.executeQuery(String.format("show tables like '%s'", name_table)).next();
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        } finally {
            connect.close();
            state.close();
        }
        return null;
    }

    /**
     * Метод для удаления индивидуального расписания из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param day Номер дня от 0 до 14, где 0 - полное удаление.
     */
    public void deleteSchedule(String id, int day) throws SQLException{
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        if (day != 0) {
            try {
                state.executeUpdate(String.format("Alter table `%s` drop column `%s`", id, day));
            } catch (SQLException ex) {
                System.out.println(ex.getErrorCode() + ex.getMessage());
            } finally {
                connect.close();
                state.close();
            }
        } else {
            dropTable(id);
            switchUserStatus(id);
        }
    }

    /**
     * Метод для записи дедлайнов на определённую дату.
     * @param id Пользователя.
     * @param deadlines Список дедлайнов для записи.
     * @param date Дата, на которую нужно получить дедлайн, в формате d.mm.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    public void setDeadlines(String id, List<String>deadlines, String date) throws SQLException {
        boolean flag;
        int count = 0;
        flag = tableIsExist("deadlines_" + id);
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        try{
            if (!flag){
                state.executeUpdate(String.format("update `users` set `existDL`='1' where `id`='%s'", id));
                state.executeUpdate(String.format("Create table `deadlines_%s`(`id` int primary key not null auto_increment, `%s` VARCHAR(60))", id, date));
            }
            if (!state.executeQuery(String.format("show columns from `deadlines_%s` like '%s'", id, date)).next()){
                state.executeUpdate(String.format("alter table `deadlines_%s` add column (`%s` VARCHAR(60))", id, date));
            }
            if (state.executeQuery(String.format("select count(`id`) from `deadlines_`", id)).next()){
                count = result.getInt(1);
            }
            int i = 0;
            for (String s : deadlines) {
                if (i < count) {
                    state.executeUpdate(String.format("update `deadlines_%s` set `%s` = '%s' where `id` = '%s'", id, date, s, i + 1));
                } else {
                    state.executeUpdate(String.format("insert into `deadlines_%s` (`%s`) values ('%s')", id, date, s));
                }
                i++;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        }
        finally {
            connect.close();
            state.close();
        }
    }

    /**
     * Метод для получения списка дедлайнов пользователя.
     * @param id Пользователя.
     * @param date Дата, на которую нужно получить дедлайн, в формате d.mm.
     * @return Возвращает список всех дедлайнов на день.
     * @throws SQLException Отсутствует таблица код ошибки 1146.
     */
    public List<String> getDeadlines(String id, String date) throws SQLException {
        boolean flag = tableIsExist("deadlines_" + id);
        List<String> deadlines = new ArrayList<>();
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        if (!flag) return deadlines;
        try{
            result = state.executeQuery(String.format("SELECT `%s` FROM `deadlines_%s`", date, id));
            while (result.next()){
                if (Objects.equals(result.getString(1), "end")) break;
                deadlines.add(result.getString(1));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        } finally {
            connect.close();
            state.close();
            result.close();
        }
        return deadlines;
    }

    /**
     * Метод для перезаписи дедлайнов на день.
     * @param id Пользователя.
     * @param newDeadlines Список новых дедлайнов для перезаписи.
     * @param date Дата, на которую нужно получить дедлайн, в формате d.mm.
     * @throws SQLException Ошибка отсутствия записей на этот день.
     */
    public void editDeadlines(String id, List<String> newDeadlines, String date) throws SQLException {
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        try {
            state.executeUpdate(String.format("alter table `deadlines_%s` drop column `%s`", id, date));
            setDeadlines(id, newDeadlines, date);
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        } finally {
            connect.close();
            state.close();
        }
    }

    /**
     * Приватный метод для удаления таблицы из базы данных.
     * @param name_table Имя таблицы в базе данных.
     */
    private void dropTable(String name_table) throws SQLException{
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        try {
            state.executeUpdate(String.format("DROP TABLE `%s`", name_table));
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        } finally {
            connect.close();
            state.close();
        }
    }

    /**
     * Приватный метод для смены значения параметра: Использовать индивидуальное расписание в базе данных.
     * @param id Идентификатор пользователя в базе данных.
     */
    private void switchUserStatus(String id) throws SQLException{
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        try{
            result = state.executeQuery(String.format("Select `useIndiv` from `users` where `id` = '%s'", id));
            if (result.next()) state.executeUpdate(String.format("Update `users` set `useIndiv` = '%d' where `id` = '%s'", (result.getInt(1) + 1) % 2, id));
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        } finally {
            connect.close();
            state.close();
            result.close();
        }

    }

    /**
     * Метод для получения из базы данных индивидуального расписания из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param day Номер дня недели, на который сохраняется расписание.
     * @return Возвращает индивидуальное расписание пользователя.
     * @throws SQLException Ошибка существования индивидуального расписания.
     */
    private List<String> getCastomSchedule (String id, int day) throws SQLException{
        List<String> schedule = null;
        String lesson = null;
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        result = state.executeQuery(String.format("Select `%s` from `%s`", day, id));
        schedule = new ArrayList<>();
        while (result.next()) {
            lesson = result.getString(1);
            if (Objects.equals(lesson, "end")) break;
            schedule.add(lesson);
        }
        connect.close();
        state.close();
        result.close();

        return schedule;
    }
}
