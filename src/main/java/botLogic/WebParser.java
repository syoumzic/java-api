package botLogic;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebParser implements Parser {

    /**
     * осуществляет получение id группы по названю для дальнейшего поиска расписания
     * @param group название группы
     * @throws NoSuchElementException группа не найдена
     * @throws IOException ошибка чтения данных
     * @return расписание на две недели или null если такой группы нет
     */
    public List<List<String>>parse(String group) throws NoSuchElementException, IOException {
        if(group.length() < 3) throw new NoSuchElementException();

        String groupId = getGroupId(group.toUpperCase());
        return getSchedule(groupId);
    }

    /**
     * Получение id по названию группы
     * @param group название группы
     * @throws NoSuchElementException группа не найдена
     * @throws IOException ошибка чтени
     * @return id группы
     */
    public String getGroupId(String group) throws NoSuchElementException, IOException {
        URL url = new URL("https://urfu.ru/api/schedule/groups/suggest/?query=" + group);
        JSONParser parser = new JSONParser();

        JSONObject document;
        try {
            document = (JSONObject) parser.parse(new InputStreamReader(url.openStream()));
        }catch(ParseException e){
            throw new IOException();
        }

        JSONArray suggestions = (JSONArray)document.get("suggestions");

        for(int i = 0; i < suggestions.size(); i++){
            JSONObject suggestion = (JSONObject)suggestions.get(i);
            String value = (String)suggestion.get("value");

            if(Objects.equals(group, value))
                return Long.toString((Long)suggestion.get("data"));
        }

        throw new NoSuchElementException();
    }

    /**
     * Получение расписаниея по id группы
     * @param id id группы
     * @throws IOException ошибка чтения данных
     * @return расписание
     */
    public List<List<String>> getSchedule(String id) throws IOException{
        LocalDate shiftDate = LocalDate.now().minusWeeks(1);

        int shiftYear = shiftDate.getYear();
        int shiftMonth = shiftDate.getMonthValue();
        int shiftDay = shiftDate.getDayOfMonth();

        Document document = Jsoup.connect(String.format("https://urfu.ru/api/schedule/groups/lessons/%s/%d%02d%02d", id, shiftYear, shiftMonth, shiftDay)).get();
        Elements documentLessons = document.select("tr");

        Calendar calendar = new Calendar();

        final int daysCount = 14;
        int day = 0;
        int shift = calendar.getShift(shiftDate);
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

            Elements filterLesson = lesson.select("td");
            if(filterLesson.size() < 2) throw new IOException();
            String lessonName = getName(lesson.select("td").get(1));

            Matcher pattern = Pattern.compile("^[0-9]+").matcher(lessonName);
            int newIndex = (pattern.find()? Integer.parseInt(pattern.group(0)) : 1);
            for(int i = 1; i < newIndex - index; i++) currentSchedule.add("-");
            index = newIndex;

            currentSchedule.add(lessonName);
        }

        return scheduleList;
    }

    /**
     * извлекает из тега информацию о предмете
     * @param lesson тег
     * @throws IOException ошибка чтения данных
     * @return информация о предмете
     */
    private String getName(Element lesson) throws IOException{
        StringBuilder lessonBuilder = new StringBuilder();
        for(int i = 0; i < lesson.childrenSize(); i++){
            Element lessonNameElement = lesson.child(i);

            if(lessonNameElement.childrenSize() == 0) throw new IOException();
            String name = lessonNameElement.child(0).text();
            lessonBuilder.append(name);

            if(!lessonNameElement.select("span:nth-child(2)").isEmpty()) {          //возможен случай, когда группа не указана
                String place = lessonNameElement.child(1).child(1).text();
                lessonBuilder.append(" ").append(place);
            }

            if(i != lesson.childrenSize() - 1)
                lessonBuilder.append("\n");
        }

        return lessonBuilder.toString();
    }
}