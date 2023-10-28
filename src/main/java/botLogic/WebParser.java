package botLogic;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class WebParser implements Parser {
    public List<List<String>>parse(String group){
        return null;
    }
}

interface Parser{
    List<List<String>> parse(String group);
}