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
public class ListHandler implements ParameterHandler{
    private Reference<List<String>>callbackSchedule;
    private Time time;

    /**
     * Конструктор класса ScheduleHandler
     * @param callbackSchedule ссылка на расписание в которое запишется считанное значение
     */
    public ListHandler(Reference<List<String>> callbackSchedule, Time time){
        this.callbackSchedule = callbackSchedule;
        this.time = time;
    }

    public String startMessage(){
        return  "Перечислите список по порядку через enter (ctrl+enter)\n" +
                "Строки перечисляются в формате: [время] [название]\n" +
                "Для указания пустого списка введите '-'\n";
    }

    /**
     * Проверяет валидность введённого расписания
     * @param message сообщение пользователя
     * @throws LogicException группа не валидна
     */
    public void handle(User user, String message) throws LogicException {
        callbackSchedule.current = new ArrayList<String>();
        int second = -1;

        if(Objects.equals(message, "-"))
            return;

        for(String lesson : message.split("\n")){
            if(Objects.equals(lesson, "")) throw new LogicException("Обнаружена пустая строчка \n" +
                                                                       "если для пустого списка введите -");

            if(lesson.length() >= 64) throw new LogicException("превышена максимальная строки");
            Pattern timePattern = Pattern.compile("^\\s*(\\d{1,2}):(\\d{2})");
            Matcher matcher = timePattern.matcher(lesson);

            if(!matcher.find()) throw new LogicException("для строки '%s' не указано время".formatted(lesson));

            int nextSecond;
            try {
                nextSecond = time.getSecondsOfDay(lesson);
            }catch(DateTimeException e){
                throw new LogicException("для строки '%s' время указано некорректно".formatted(lesson));
            }

            if(nextSecond <= second) throw new LogicException("Элементы идёт не по порядку");
            second = nextSecond;

            callbackSchedule.current.add(lesson);
        }
    }
}
