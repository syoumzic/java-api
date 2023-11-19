package botLogic.parameterHandler;

import botLogic.LogicException;
import botLogic.User;
import botLogic.utils.Reference;
import botLogic.utils.Time;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Считыватель предметов
 */
public class ScheduleHandler implements ParameterHandler{
    private Reference<List<String>>callbackSchedule;
    private Time time;
    /**
     * Конструктор класса ScheduleHandler
     * @param callbackSchedule ссылка на расписание в которое запишется считанное значение
     */
    public ScheduleHandler(Reference<List<String>> callbackSchedule, Time time){
        this.callbackSchedule = callbackSchedule;
        this.time = time;
    }

    public String startMessage(){
        return  "Перечислите предметы по порядку через enter (ctrl+enter) (если предмета нет, то ставить '-')\n" +
                "Предметы перечисляются в формате: [время] [кабинет] [название предмета]\n" +
                "Кабинет является необязательным параметром\n" +
                "\n" +
                "Пример:\n" +
                "9:00 514 Основы проектной деятельности\n" +
                "10:40 632 Объектно-ориентированное программирование \n" +
                "16:00 Прикладная физическая культура";
    }

    /**
     * Проверяет валидность введённого расписания
     * @param message сообщение пользователя
     * @throws LogicException группа не валидна
     */
    public void handle(User user, String message) throws LogicException {
        callbackSchedule.current = new ArrayList<String>();
        int minute = -1;
        for(String lesson : message.split("\n")){
            if(Objects.equals(lesson, "")) throw new LogicException("обнаружена пустая строчка \n" +
                                                                       "если предмета нет, необходимо ставить '-'");
            if(lesson.length() >= 64) throw new LogicException("превышена макимальная длинна на предмета");
            Pattern timePattern = Pattern.compile("^\\s*(\\d{1,2}):(\\d{2})");
            Matcher matcher = timePattern.matcher(lesson);

            if(!matcher.find()) throw new LogicException("для строки '%s' не указано время".formatted(lesson));

            int nextMinute;
            try {
                nextMinute = time.getTime(lesson);
            }catch(DateTimeException e){
                throw new LogicException("для строки '%s' время указано некорректно".formatted(lesson));
            }

            if(nextMinute <= minute) throw new LogicException("Расписание идёт не по порядку");
            minute = nextMinute;

            callbackSchedule.current.add(lesson);
        }
    }
}
