package com.telegrambot.friday.botApi.service.holders;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.service.MessageGenerator;
import com.telegrambot.friday.botApi.state.BotState;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommandNotSetState implements MessageHolder{
    UserCache userCache;
    final MessageGenerator generator;

    public CommandNotSetState(UserCache userCache, MessageGenerator generator) {
        this.userCache = userCache;
        this.generator = generator;
    }

    @Override
    public SendMessage handle(Message message) {
        return generateMessage(message);
    }

    @Override
    public BotState getStateHandlerName() {
        return BotState.COMMAND_NOT_SET;
    }

    private SendMessage generateMessage(Message message) {
        long userID = message.getFrom().getId();
        long chatID = message.getChatId();

        userCache.setBotState(userID, null);
        return generator.generateMessage(chatID, "Неизвестная комманда!");
    }
}
