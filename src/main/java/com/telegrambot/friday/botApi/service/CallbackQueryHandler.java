package com.telegrambot.friday.botApi.service;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.state.BotState;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackQueryHandler {
    final BotStateContext context;
    final MessageGenerator generator;
    UserCache userCache;

    public CallbackQueryHandler(BotStateContext context, MessageGenerator generator, UserCache userCache) {
        this.context = context;
        this.generator = generator;
        this.userCache = userCache;
    }

    public SendMessage replyMessage(CallbackQuery callbackQuery) {
        String userQuery = callbackQuery.getData();
        long chatID = callbackQuery.getMessage().getChatId();
        long userID = callbackQuery.getFrom().getId();
        BotState botState = userCache.getBotState(userID);

        switch (userQuery) {
            case "setNewCity" -> {
                userCache.setBotState(userID, BotState.SET_CITY);
                return generator.generateMessage(chatID, "Введите название города...");
            }
            case "getUserLocation" -> botState = BotState.LOCATION_MENU;
        }

        userCache.setBotState(userID, botState);

        return context.getMessageByBotState(botState, callbackQuery.getMessage());
    }
}
