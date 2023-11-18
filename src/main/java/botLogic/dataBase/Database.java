package botLogic.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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
    public List<String> getSchedule(String id, int day) throws SQLException {
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
     * Метод для получения из базы данных индивидуального расписания из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param day Номер дня недели, на который сохраняется расписание.
     * @return Возвращает индивидуальное расписание пользователя.
     * @throws SQLException Ошибка существования индивидуального расписания.
     */
    public List<String> getCastomSchedule (String id, int day) throws SQLException {
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
    public void addUserGroup(String id, String group) throws SQLException {
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

    public void setUserTime(String id, int time) throws SQLException {
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
     * Метод для смены значения параметра: Использовать индивидуальное расписание в базе данных.
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
     * Метод для смены статуса уведомлений пользователя в базе данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param status Принимает значения: 1 - уведомления включены / 0 - выключены.
     * @throws SQLException Ошибка доступа к базе данных.
     */
    public void switchNotifications(String id, int status) throws SQLException{
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
     * Метод для получения номера группы пользователя из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @return Возвращает номер группы.
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
}
