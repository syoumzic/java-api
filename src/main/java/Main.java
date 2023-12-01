import JavaBots.Bot;
import JavaBots.DiscordBot.Discord_Bot;
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

        String botName = "SUPBot";
        String tgBotToken = System.getenv("TG_TOKEN");
        String dsBotToken = System.getenv("DS_TOKEN");
        Bot tgBot = new Telegram_Bot(botName, tgBotToken, logic);
        Bot dsBot = new Discord_Bot(dsBotToken, logic);

        logic.updateNotification(tgBot, dsBot);
    }
}

