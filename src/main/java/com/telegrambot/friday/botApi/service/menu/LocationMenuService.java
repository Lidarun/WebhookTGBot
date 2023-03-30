package com.telegrambot.friday.botApi.service.menu;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationMenuService {
    public SendMessage getMenu(long chatID, String message){
        ReplyKeyboardMarkup replyKeyboardMarkup = getLocationMenuKeyboard();
        SendMessage sendMessage = createMessageWithKeyboard(chatID, message, replyKeyboardMarkup);
        return sendMessage;
    }

    private SendMessage createMessageWithKeyboard(long chatID, String message,
                                                  ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdownV2(true);
        sendMessage.enableNotification();
        sendMessage.setChatId(chatID);
        sendMessage.setText(message);
        if (replyKeyboardMarkup != null)
            sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    private ReplyKeyboardMarkup getLocationMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRowList  = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton locationButton = new KeyboardButton();

        locationButton.setText("Определить местоположение");
        locationButton.setRequestLocation(true);

        row1.add(locationButton);
        keyboardRowList.add(row1);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
