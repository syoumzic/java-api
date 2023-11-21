import JavaBots.Bot;
import JavaBots.TelegramBot.Telegram_Bot;
import botLogic.Logic;
import botLogic.dataBase.Data;
import botLogic.dataBase.Database;
import botLogic.parser.Parser;
import botLogic.parser.WebParser;
import botLogic.utils.Calendar;
import botLogic.utils.Time;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
    public static void main(String[] args) {
        Data dataBase = new Database();
        Parser parser = new WebParser();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Time time = new Calendar();

        Logic logic = new Logic(dataBase, parser, time, scheduler);

        String botName = "echo";
        String botToken = System.getenv("BOT_TOKEN");
        Bot tg_bot = new Telegram_Bot(botName, botToken, logic);

        logic.updateNotification(tg_bot);
    }
}

