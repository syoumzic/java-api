package botLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


/**
 * Класс для работы с базой данных, чтение и запись.
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
        List<String> schedule;
        try {
            return getCastomSchedule(id, day);
        } catch (SQLException ex) {
            connect = DriverManager.getConnection(url, user, password);
            state = connect.createStatement();
            result = state.executeQuery(String.format("SELECT `group` FROM `users` WHERE id='%s'", id));
            if (result.next()) group = result.getString(1);
            result = state.executeQuery(String.format("SELECT `%s` FROM `%s`", day, group.toLowerCase()));
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

    /**
     * Метод для записи расписания в базу данных.
     * @param group Номер группы пользователя.
     * @param schedule Расписание для записи в базу данных.
     */
    public void setSchedule(String group, List<List<String>> schedule){
        try {
            connect = DriverManager.getConnection(url, user, password);
            state = connect.createStatement();
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
            connect.close();
            state.close();
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        }
    }

    /**
     * Метод для записи в базу данных индивидуального расписания пользователя.
     * @param id Идентификатор пользователя в базе данных.
     * @param schedule Индивидуальное расписание для записи.
     * @param day Номер дня недели, на который сохраняется расписание.
     */
    public void setCastomSchedule(String id, List<String> schedule, int day){
        try {
            connect = DriverManager.getConnection(url, user, password);
            state = connect.createStatement();
            if (!tableIsExist(id))
                state.executeUpdate(String.format("Create table `%s`(`id` int primary key not null auto_increment)", id));
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
     * Метод добавляющий пользователя в базу данных.
     * @param id Идентификатор пользователя в базе данных.
     * @param group Номер группы пользователя.
     */
    public void addUserGroup(String id, String group){
        try {
            connect = DriverManager.getConnection(url, user, password);

            state = connect.createStatement();

            if (state.executeQuery(String.format("Select `id` from `users` where `id` = '%s'", id)).next()){
                state.executeUpdate(String.format("UPDATE `users` SET `group` = '%s' WHERE `id` = '%s'", group, id));
            }
            else {
                state.executeUpdate(String.format("INSERT INTO `users` (`id`, `group`) VALUES ('%s', '%s')", id, group));
            }

            connect.close();

            state.close();
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        }
    }

    /**
     * Метод для получения номера группы пользователя из базы данных.
     * @param id Идентификатор пользователя в базе данных.
     * @return Возвращает номер группы.
     */
    public String getUsersGroup(String id){
        String group = null;
        try {
            connect = DriverManager.getConnection(url, user, password);

            state = connect.createStatement();

            result = state.executeQuery(String.format("SELECT `group` FROM `users` WHERE `id` = '%s'", id));

            if (result.next()) group = result.getString(1);

            connect.close();

            state.close();
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        }
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

        return state.executeQuery(String.format("show tables like '%s'", name_table)).next();
    }

    /**
     * Приватный метод для удаления таблицы из базы данных.
     * @param name_table Имя таблицы в базе данных.
     */
    private void dropTable(String name_table){
        try {
            connect = DriverManager.getConnection(url, user, password);

            state = connect.createStatement();

            state.executeUpdate(String.format("DROP TABLE `%s`", name_table));

            connect.close();

            state.close();

        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode() + ex.getMessage());
        }
    }
}
