package botLogic.parameterHandler;

import botLogic.LogicException;
import botLogic.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduleHandler implements ParameterHandler{
    private Reference<List<String>>callbackSchedule;

    public ScheduleHandler(Reference<List<String>> callbackSchedule){
        this.callbackSchedule = callbackSchedule;
    }

    public String startMessage(){
        return  "Перечислите предметы по порядку через enter (ctrl+enter) (если предмета нет, то ставить '-')\n" +
                "Предметы перечисляются в формате: [дата] [кабинет] [название предмета]\n" +
                "Кабинет является необязательным параметром\n" +
                "Пример:\n" +
                "   9:00 514 Основы проектной деятельности\n" +
                "   10:40 632 Объектно-ориентированное программирование \n" +
                "   16:00 Прикладная физическая культура";
    }
    public void handle(String message) throws LogicException {
        callbackSchedule.current = new ArrayList<String>();

        for(String lesson : message.split("\n")){
            if(Objects.equals(lesson, "")) throw new RuntimeException("обнаружена пустая строчка \n" +
                                                        "если предмета нет, необходимо ставить '-'");
            if(lesson.length() >= 64) throw new RuntimeException("превышена макимальная длинна на предмета");
            if(!Pattern.matches("\\s*\\d{1,2}:\\d{2}\\s+[А-Яa-яA-Za-z]+$", lesson)) throw new LogicException(String.format("строчка '%s' не соответствует формату", lesson));
            callbackSchedule.current.add(lesson);
        }
    }
}
