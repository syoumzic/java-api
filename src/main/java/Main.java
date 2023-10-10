import JavaBots.Bot;
import JavaBots.TelegramBot.Telegram_Bot;

public class Main {
    public static void main(String[] args) {
        String botName = "echo";
        String botToken = System.getenv("BOT_TOKEN");
        Bot tg_bot = new Telegram_Bot(botName, botToken);
    }
}

