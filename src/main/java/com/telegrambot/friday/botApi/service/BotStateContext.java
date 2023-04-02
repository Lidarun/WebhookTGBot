package com.telegrambot.friday.botApi.service;

import com.telegrambot.friday.botApi.config.BotState;
import com.telegrambot.friday.botApi.service.holders.MessageHolder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotStateContext {
    Map<BotState, MessageHolder> botStateMessages = new HashMap<>();

    public BotStateContext(List<MessageHolder> messageHandlers) {
        messageHandlers.forEach(handler -> botStateMessages.put(handler.getStateHandlerName(), handler));
    }

    public SendMessage getMessageByBotState(BotState botState, Message message) {
        MessageHolder handler = findMessageHandlerByBotState(botState);
        return handler.handle(message);
    }

    private MessageHolder findMessageHandlerByBotState(BotState botState) {
        return botStateMessages.get(botState);
    }


}
