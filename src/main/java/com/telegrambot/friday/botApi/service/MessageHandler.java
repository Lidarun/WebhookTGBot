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

        System.out.println(" + " + botState);
        switch (userMessage) {
            case "/start" -> botState = BotState.START;
            case "/weather" -> botState = BotState.WEATHER;
            case "/setcity" -> botState = BotState.SET_CITY;

            default -> botState = BotState.COMMAND_NOT_SET;
        }


        System.out.println(" - " + botState);
        return stateHandler.replyMessage(botState, message);
    }
}
