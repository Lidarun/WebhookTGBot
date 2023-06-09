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

            if (botState == null) {
                switch (userMessage) {
                    case "/start":
                        botState = BotState.START;
                        break;
                    case "Погода":
                    case "/weather":
                        botState = BotState.WEATHER;
                        break;
                    case "/setcity":
                        botState = BotState.SET_CITY;
                        break;
                    default:
                        botState = BotState.COMMAND_NOT_SET;
                        break;
                }
            }
            userCache.setBotState(userID, botState);

            return context.getMessageByBotState(botState, message);
    }
}