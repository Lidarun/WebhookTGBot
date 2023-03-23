package com.telegrambot.friday.botApi.cache;

import com.telegrambot.friday.botApi.service.MessageHandler;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;

//@Component
public class BotStateContext {
    private Map<BotState, MessageHandler> messageHandlerMap = new HashMap<>();

    public BotStateContext(BotState state, Message message) {
//        MessageHandler handler =
    }
}
