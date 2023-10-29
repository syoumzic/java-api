package botLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class Database implements Data {
    private final String url = System.getenv("URL");
    private final String user = System.getenv("NAMEUSER");
    private final String password = System.getenv("PASSUSER");

    // JDBC variables for opening and managing connection
    private Connection connect;
    private Statement state;
    private ResultSet result;

    public List<String> getSchedule(String id, int day) {
        //String query = "";

        try {
            // opening database connection to MySQL server
            connect = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            state = connect.createStatement();

            // executing SELECT query
            //result = state.executeQuery(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            //close Connection, Statement and resultSet here
            try {
                connect.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                state.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                result.close();
            } catch (SQLException se) { /*can't do anything */ }
        }
        return null;
    }

    public void setSchedule(String group, List<List<String>> schedule){
    }

    public void addUser(String id, String group){
    }

    public void updateUser(String id, String group){
    }
}
