package botLogic;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public interface Parser{
    List<List<String>> parse(String group) throws IOException, ParseException;
}