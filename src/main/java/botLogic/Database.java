package botLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Database implements Data {
    private final String url = System.getenv("URL");
    private final String user = System.getenv("NAMEUSER");
    private final String password = System.getenv("PASSUSER");

    // JDBC variables for opening and managing connection
    private Connection connect;
    private Statement state;
    private ResultSet result;

    public List<String> getSchedule(String id, int day) throws RuntimeException {
        String group = "";
        List<String> schedule;
        try {
            connect = DriverManager.getConnection(url, user, password);
            state = connect.createStatement();
            result = state.executeQuery("SELECT `group` FROM `users` WHERE id='12839392'");
            if (result.next()) group = result.getString(1);
            result = state.executeQuery("SELECT * FROM `" + group + "`");
            schedule = new ArrayList<String>();
            while (result.next()){
                schedule.add(result.getString(day));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            //close Connection, Statement and resultSet here
            try {
                connect.close();
                state.close();
                result.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return schedule;
    }

    public void setSchedule(String group, List<List<String>> schedule) throws RuntimeException {
        try {
            connect = DriverManager.getConnection(url, user, password);
            state = connect.createStatement();
            result = state.executeQuery("");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            //close Connection, Statement and resultSet here
            try {
                connect.close();
                state.close();
                result.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addUser(String id, String group){
    }

    public void updateUser(String id, String group){
    }
}
