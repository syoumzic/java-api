package botLogic;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public interface Parser{
    List<List<String>> parse(String group) throws IOException, NoSuchElementException;
}