package com.telegrambot.friday.botApi.service;

import com.telegrambot.friday.botApi.state.BotState;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  MessageHandler {
    final StateHandler stateHandler;
    BotState botState;

    public MessageHandler(StateHandler stateHandler) {
        this.stateHandler = stateHandler;
    }

    public BotApiMethod<?> replyMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        String userMessage = message.getText();
        sendMessage.setChatId(message.getChatId());


        switch (userMessage) {
            case "/start" -> botState = BotState.START;
        }

        BotApiMethod<?> msg = stateHandler.replyMessage(botState, message);
        botState = stateHandler.getBotState();
        System.out.println(botState);
        return msg;
    }
}
