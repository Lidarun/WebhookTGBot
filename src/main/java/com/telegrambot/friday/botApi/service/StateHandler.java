package com.telegrambot.friday.botApi.service;

import com.telegrambot.friday.botApi.state.BotState;
import com.telegrambot.friday.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Getter
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StateHandler {
    UserService userService;
    BotState botState;

    public StateHandler(UserService userService) {
        this.userService = userService;
    }

    public BotApiMethod<?> replyMessage(BotState botState, Message message) {
        SendMessage sendMessage = new SendMessage();
        String userMessage = message.getText();
        sendMessage.setChatId(message.getChatId());

        switch (botState) {
            case START -> {
                userService.save(message);
                sendMessage.setText("Приветствую, " + message.getChat().getFirstName() + "!");
                botState = BotState.COMMAND_NOT_SET;
                return sendMessage;
            }
        }
        return null;
    }
}
