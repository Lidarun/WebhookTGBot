package com.telegrambot.friday.botApi.service;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.state.BotState;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  MessageHandler {
    final BotStateContext context;
    UserCache userCache;

    public MessageHandler(BotStateContext context, UserCache userCache) {
        this.context = context;
        this.userCache = userCache;
    }

    public BotApiMethod<?> replyMessage(Message message) {
        String userMessage = message.getText();


        long userID = message.getFrom().getId();
        BotState botState = userCache.getBotState(userID);

        if(botState == null) {
            switch (userMessage) {
                case "/start" -> botState = BotState.START;
                case "/weather" -> botState = BotState.WEATHER;
                case "/setcity" -> botState = BotState.SET_CITY;
                default -> botState = BotState.COMMAND_NOT_SET;
            }
        }
        userCache.setBotState(userID, botState);

        return context.getMessageByBotState(botState, message);
    }
}
