import JavaBots.Bot;
import JavaBots.TelegramBot.Telegram_Bot;
import botLogic.Logic;

public class Main {
    public static void main(String[] args) {
        Logic logic = new Logic();

        String botName = "echo";
        String botToken = System.getenv("BOT_TOKEN");
        Bot tg_bot = new Telegram_Bot(botName, botToken, logic);
    }
}

