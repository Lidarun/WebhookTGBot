package com.telegrambot.friday.botApi.service;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.config.BotState;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationHandler {
    final BotStateContext context;
    UserCache userCache;

    public LocationHandler(BotStateContext context, UserCache userCache) {
        this.context = context;
        this.userCache = userCache;
    }

    public BotApiMethod<?> replyMessage(Message message) {
        long userID = message.getFrom().getId();
        BotState botState = BotState.WEATHER_BY_LOCATION;

        userCache.setBotState(userID, botState);

        return context.getMessageByBotState(botState, message);
    }
}