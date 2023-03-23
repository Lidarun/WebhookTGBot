package com.telegrambot.friday.botApi.controller;

import com.telegrambot.friday.botApi.service.MessageHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Friday extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;

    MessageHandler messageHandler;
//    CallbackQueryHandler callbackQueryHandler;

    public Friday(SetWebhook setWebhook, MessageHandler messageHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return null;
    }

    private BotApiMethod<?> handleUpdate(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            return messageHandler.replyMessage(update.getMessage());
        }

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return null; // CALLBACK later
        }

        return null;
    }
}
