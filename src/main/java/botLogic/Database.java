package botLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


//1050; //SQLSTATE: 42S01 Message: Table '%s' already exists

public class Database implements Data {
    private final String url = System.getenv("URL");
    private final String user = System.getenv("NAMEUSER");
    private final String password = System.getenv("PASSUSER");

    // JDBC variables for opening and managing connection
    private Connection connect;
    private Statement state;
    private ResultSet result;

    public List<String> getSchedule(String id, int day) throws SQLException{
        String group = "";
        List<String> schedule;
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        result = state.executeQuery(String.format("SELECT `group` FROM `users` WHERE id='%s'", id));
        if (result.next()) group = result.getString(1);
        result = state.executeQuery(String.format("SELECT `%s` FROM `%s`", day, group));
        schedule = new ArrayList<>();
        while (result.next()) {
            schedule.add(result.getString(1));
        }
        connect.close();
        state.close();
        result.close();
        return schedule;
    }

    public void setSchedule(String group, List<List<String>> schedule) throws SQLException {
        connect = DriverManager.getConnection(url, user, password);
        state = connect.createStatement();
        if (state.executeQuery(String.format("show tables like '%s'", group)).next()){
            dropTable(group);
        }
        StringBuilder query = new StringBuilder(String.format("CREATE TABLE `%s` (`id` INT NOT NULL AUTO_INCREMENT,", group));
        for (int i = 0; i < 14; i ++){
            query.append(String.format(" `%s` VARCHAR(64) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL DEFAULT '-',", (i + 1)));
        }
        query.append(" primary key (id)) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8");
        state.executeUpdate(query.toString());
        Iterator<String> iter;
        int max_iter = 0;
        int index;
        for (int i = 0; i < 14; i++){
            iter = schedule.get(i).iterator();
            index = 0;
            while (iter.hasNext() && index < max_iter){
                index++;
                state.executeUpdate(String.format("UPDATE `%s` SET `%s` = '%s' WHERE `id` = '%s'", group, i + 1, iter.next(), index));
            }
            while (iter.hasNext()){
                state.executeUpdate(String.format("INSERT INTO `%s` (`%s`) VALUES ('%s')", group, i + 1, iter.next()));
            }
            max_iter = Integer.max(max_iter, schedule.get(i).size());
        }
        connect.close();
        state.close();
    }

    public void addUser(String id, String group) throws SQLException{
        connect = DriverManager.getConnection(url, user, password);

        state = connect.createStatement();

        state.executeUpdate(String.format("INSERT INTO `users` (`id`, `group`) VALUES ('%s', '%s')", id, group));

        connect.close();

        state.close();
    }

    public void updateUser(String id, String group) throws SQLException{
        connect = DriverManager.getConnection(url, user, password);

        state = connect.createStatement();

        state.executeUpdate(String.format("UPDATE `users` SET `group` = '%s' WHERE `id` = '%s'", group, id));

        connect.close();

        state.close();
    }

    public String getUsersGroup(String id) throws SQLException{
        String group;
        connect = DriverManager.getConnection(url, user, password);

        state = connect.createStatement();

        result = state.executeQuery(String.format("SELECT `group` FROM `users` WHERE `id` = '%s'", id));

        group = result.getString(1);

        connect.close();

        state.close();

        return group;
    }
    public void dropTable(String name_table) throws SQLException{
        connect = DriverManager.getConnection(url, user, password);

        state = connect.createStatement();

        state.executeUpdate(String.format("DROP TABLE `%s`", name_table));

        connect.close();

        state.close();
    }
}
