package JavaBots.TelegramBot;

import JavaBots.Bot;
import botLogic.Logic;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Класс для обработки сообщений пользователя Telegram
 */
public class Telegram_Bot extends TelegramLongPollingBot implements Bot {
    final String botName;
    final String botToken;
    Logic logic;

    /**
     * Конструктор класса Telegram_Bot
     * @param botName имя бота
     * @param botToken токен бота
     * @param logic класс логики
     */
    public Telegram_Bot(String botName, String botToken, Logic logic) {
        this.botName = botName;
        this.botToken = botToken;
        this.logic = logic;

        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
            System.out.println("JavaBots.Bot is working");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    /**
     * Обработчик сообщений
     * @param update обновление сообщений пользователей
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            String chat_id = update.getMessage().getChatId().toString();

            SendMessage message = new SendMessage();
            message.setChatId(chat_id);
            message.setText(logic.processMessage(chat_id, message_text));

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Отправка сообщения пользователю
     * @param chatId id чата
     * @param text текст сообщения
     */
    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId.toString());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}