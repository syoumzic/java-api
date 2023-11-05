package botLogic;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface Data {
    List<String> getSchedule(String id, int day) throws SQLException;
    void setSchedule(String group, List<List<String>> schedule);
    void setCastomSchedule(String id, List<String> schedule, int day);
    String getNextLesson (String id, int day);
    void addUserGroup(String id, String group);
    String getUsersGroup(String id);
    void switchUserStatus(String id);
    void deleteSchedule(String id, int day);
    Boolean tableIsExist(String name_table) throws SQLException;

}