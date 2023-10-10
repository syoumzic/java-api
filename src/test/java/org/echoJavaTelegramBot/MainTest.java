package org.echoJavaTelegramBot;

import JavaBots.TelegramBot.Telegram_Bot;
import org.junit.*;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainTest{
    static Telegram_Bot bot;
    static String botName = "echo";
    static String botToken = System.getenv("BOT_TOKEN");

    @BeforeClass
    static public void botTokenValidity(){
        Assert.assertNotNull(botToken);
        bot = new Telegram_Bot(botName, botToken);
    }

    @Test
    public void getBotName(){
        Assert.assertEquals(bot.getBotUsername(), botName);
    }

    @Test
    public void getBotToken(){
        Assert.assertEquals(bot.getBotToken(), botToken);
    }
}
