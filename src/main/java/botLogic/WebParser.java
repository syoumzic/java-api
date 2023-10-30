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

//https://urfu.ru/api/schedule/groups/suggest/?query=<group>
//https://urfu.ru/api/schedule/groups/lessons/<id>/<YYYYMMDD>

public class WebParser implements Parser {

    public List<List<String>>parse(String group) throws IOException, ParseException {
        return null;
    }

    public Long getGroupId(String group) throws IOException, ParseException {
        if(group.length() < 3) return null; // при запросе выдаст невалидную страницу

        URL url = new URL("https://urfu.ru/api/schedule/groups/suggest/?query=" + group);
        JSONParser parser = new JSONParser();
        Object rawDocument = parser.parse(new InputStreamReader(url.openStream()));

        JSONObject document = (JSONObject) parser.parse(new InputStreamReader(url.openStream()));
        JSONArray suggestions = (JSONArray)document.get("suggestions");

        for(int i = 0; i < suggestions.size(); i++){
            JSONObject suggestion = (JSONObject)suggestions.get(i);
            String value = (String)suggestion.get("value");

            if(Objects.equals(group, value)) return (Long)suggestion.get("data");
        }

        return null;
    }
}