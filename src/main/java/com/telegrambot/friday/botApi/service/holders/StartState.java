package com.telegrambot.friday.botApi.service.holders;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.service.MessageGenerator;
import com.telegrambot.friday.botApi.state.BotState;
import com.telegrambot.friday.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartState implements MessageHolder {
    UserCache userCache;
    MessageGenerator generator;
    UserService userService;

    public StartState(UserCache userCache, MessageGenerator generator, UserService userService) {
        this.userCache = userCache;
        this.generator = generator;
        this.userService = userService;
    }

    @Override
    public SendMessage handle(Message message) {
        return generateMessage(message);
    }

    @Override
    public BotState getStateHandlerName() {
        return BotState.START;
    }

    private SendMessage generateMessage(Message message) {
        long userID = message.getFrom().getId();
        long chatID = message.getChatId();

        userService.save(message);
        userCache.setBotState(userID, null);

        return generator.generateMessage(chatID,
                "Приветствую, " + message.getChat().getFirstName() + "!");
    }
}
