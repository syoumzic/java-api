package botLogic;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface Data {
    List<String> getSchedule(String id, int day) throws SQLException;
    void setSchedule(String group, List<List<String>> schedule);
    void addUserGroup(String id, String group);
    String getUsersGroup(String id);

}