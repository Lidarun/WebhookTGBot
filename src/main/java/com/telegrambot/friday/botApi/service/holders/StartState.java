package com.telegrambot.friday.botApi.service.holders;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.config.BotState;
import com.telegrambot.friday.botApi.service.MessageGenerator;
import com.telegrambot.friday.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartState implements MessageHolder {
    final UserCache userCache;
    final MessageGenerator generator;
    final UserService userService;

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
        SendMessage sendMessage = generator.generateMessage(chatID,
                "Приветствую, " + message.getChat().getFirstName() + "!");
        sendMessage.setReplyMarkup(getMainMenu());

        return sendMessage;
    }

    private ReplyKeyboardMarkup getMainMenu() {
        ReplyKeyboardMarkup mainKeyboardMarkup = new ReplyKeyboardMarkup();
        mainKeyboardMarkup.setResizeKeyboard(true);
        mainKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRowList  = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Погода");

        keyboardRowList.add(row1);

        mainKeyboardMarkup.setKeyboard(keyboardRowList);
        return mainKeyboardMarkup;
    }
}
