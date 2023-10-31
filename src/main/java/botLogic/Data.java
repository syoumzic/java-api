package botLogic;
import java.sql.SQLException;
import java.util.List;

interface Data {
    List<String> getSchedule(String id, int day) throws SQLException;
    void setSchedule(String group, List<List<String>> schedule) throws SQLException;
    void addUser(String id, String group) throws SQLException;
    void updateUser(String id, String group) throws SQLException;
    String getUsersGroup(String id) throws SQLException;
    void dropTable(String name_table) throws SQLException;
}