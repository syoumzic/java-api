package botLogic;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface Data {
    List<String> getSchedule(String id, int day) throws SQLException;
    void setSchedule(String group, List<List<String>> schedule) throws SQLException;
    void setCastomSchedule(String id, List<String> schedule, int day) throws SQLException;
    String getNextLesson (String id, int day, int current_time) throws SQLException;
    void addUserGroup(String id, String group) throws SQLException;
    String getUsersGroup(String id) throws SQLException;
    void switchUserStatus(String id) throws SQLException;
    void deleteSchedule(String id, int day) throws SQLException;
    Boolean tableIsExist(String name_table) throws SQLException;
}