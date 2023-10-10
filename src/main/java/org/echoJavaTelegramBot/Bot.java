package org.echoJavaTelegramBot;

import org.telegram.telegrambots.meta.api.objects.Update;

interface Bot{
    String getBotUsername();
    String getBotToken();
    void onUpdateReceived(Update update);
}