package botLogic;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//https://urfu.ru/api/schedule/groups/suggest/?query=<group>
//https://urfu.ru/api/schedule/groups/lessons/<id>/<YYYYMMDD>

public class WebParser implements Parser {

    public List<List<String>>parse(String group) throws IllegalArgumentException, IOException, ParseException {
        String groupId = getGroupId(group);
        if(groupId == null) return null;
        return getSchedule(groupId);
    }

    public String getGroupId(String group) throws IllegalArgumentException, IOException, ParseException {
        if(group.length() < 3) throw new IllegalArgumentException();

        URL url = new URL("https://urfu.ru/api/schedule/groups/suggest/?query=" + group);
        JSONParser parser = new JSONParser();
        Object rawDocument = parser.parse(new InputStreamReader(url.openStream()));

        JSONObject document = (JSONObject) parser.parse(new InputStreamReader(url.openStream()));
        JSONArray suggestions = (JSONArray)document.get("suggestions");

        for(int i = 0; i < suggestions.size(); i++){
            JSONObject suggestion = (JSONObject)suggestions.get(i);
            String value = (String)suggestion.get("value");

            if(Objects.equals(group, value))
                return Long.toString((Long)suggestion.get("data"));
        }

        return null;
    }

    List<List<String>>getSchedule(String id){
        return null;
    }
}