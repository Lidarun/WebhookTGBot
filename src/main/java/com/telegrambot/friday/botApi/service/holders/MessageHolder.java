package com.telegrambot.friday.botApi.service.holders;

import com.telegrambot.friday.botApi.state.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHolder {
    SendMessage handle(Message message);
    BotState getStateHandlerName();
}
