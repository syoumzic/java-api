package botLogic;
import java.util.List;

public interface Data {
    List<String> getSchedule(String id, int day);
    void setSchedule(String group, List<List<String>> schedule);
    void addUser(String id, String group);
    void updateUser(String id, String group);
}