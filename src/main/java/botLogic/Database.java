package botLogic;
import java.util.List;

interface Database {
    List<String> getSchedule(String id, int day);
    void setSchedule(String group, List<List<String>> schedule);
    void updateSchedule(String id);
    void addUser(String id, String group);
    void updateUser(String id, String group);
}