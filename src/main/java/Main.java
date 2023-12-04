import JavaBots.Bot;
import JavaBots.DiscordBot.Discord_Bot;
import JavaBots.TelegramBot.Telegram_Bot;
import botLogic.Logic;
import dataBase.Data;
import dataBase.Database;
import parser.Parser;
import parser.WebParser;
import utils.Calendar;
import utils.Time;

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

