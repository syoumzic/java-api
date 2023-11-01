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

    public List<List<String>> getSchedule(String id) throws IllegalArgumentException, IOException, ParseException {
        if(id == null) throw new IllegalArgumentException();

        LocalDate shiftDate = LocalDate.now().minusWeeks(1);
        LocalDate firstDayOfEvenWeek = firstDayOfEvenWeek(shiftDate);

        int shiftYear = shiftDate.getYear();
        int shiftMonth = shiftDate.getMonthValue();
        int shiftDay = shiftDate.getDayOfMonth();

        Document document = Jsoup.connect(String.format("https://urfu.ru/api/schedule/groups/lessons/%s/%d%02d%02d", id, shiftYear, shiftMonth, shiftDay)).get();
        Elements documentLessons = document.select("tr");

        final int daysCount = 14;
        int day = 0;
        int shift = (int)firstDayOfEvenWeek.until(shiftDate, ChronoUnit.DAYS);
        int index = 0;

        List<List<String>>scheduleList = new ArrayList<List<String>>(daysCount);
        for(int i = 0; i < daysCount; i++) scheduleList.add(new ArrayList<String>());

        List<String>currentSchedule = null;

        for(Element lesson : documentLessons){
            if(lesson.className().equals("divide") && !lesson.select("b").isEmpty()){               //тег, после которого начинается теги предметов на день
                currentSchedule = scheduleList.get((day + shift) % daysCount);
                index = 0;
                continue;
            }

            if(lesson.className().equals("divide")) {                                                       //тег, после которого заканчиваются теги предметов на день
                currentSchedule.add("end");                                                                 //строка конца предметов
                day++;
                continue;
            }

            if(lesson.className().equals("shedule-weekday-row shedule-weekday-first-row")){                //тег пустого расписания
                continue;
            }

            String lessonName = "";
            String lessonCabinet = "";

            Element lessonNameElement = lesson.select("dl:nth-child(1)").get(0);

            lessonName = lessonNameElement.child(0).text();
            Elements cabinetSelectorResult = lessonNameElement.select("span:nth-child(2)");
            if(!cabinetSelectorResult.isEmpty()) lessonCabinet = cabinetSelectorResult.get(0).text();

            Matcher pattern = Pattern.compile("^[0-9]+").matcher(lessonName);
            int newIndex = (pattern.find()? Integer.parseInt(pattern.group(0)) : 1);
            for(int i = 1; i < newIndex - index; i++) currentSchedule.add("-");
            index = newIndex;

            currentSchedule.add(lessonName + " " + lessonCabinet);
        }

        return scheduleList;
    }

    private static LocalDate firstDayOfEvenWeek(LocalDate date){
        LocalDate currentDate = LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        int weekOfYear = currentDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);

        if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) //неделя в России начинается не с воскресенья
            weekOfYear -= 1;

        if (weekOfYear % 2 == 0)currentDate = currentDate.with(ChronoField.DAY_OF_WEEK, 1);
        else currentDate = currentDate.minusWeeks(1).with(ChronoField.DAY_OF_WEEK, 1);

        return currentDate;
    }
}