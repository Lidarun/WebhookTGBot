package com.telegrambot.friday.botApi.service;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.state.BotState;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackQueryHandler {
    final MessageGenerator generator;
    UserCache userCache;

    public CallbackQueryHandler(MessageGenerator generator, UserCache userCache) {
        this.generator = generator;
        this.userCache = userCache;
    }

    public SendMessage replyMessage(CallbackQuery callbackQuery) {
        String userQuery = callbackQuery.getData();
        long chatID = callbackQuery.getMessage().getChatId();
        long userID = callbackQuery.getFrom().getId();
        BotState botState = userCache.getBotState(userID);

        SendMessage sendMessage = new SendMessage();

        switch (userQuery) {
            case "setNewCity" -> {
                botState = BotState.SET_CITY;
                sendMessage = generator.generateMessage(chatID, "Введите название города...");
            }
        }

        userCache.setBotState(userID, botState);

        return sendMessage;
    }
}
