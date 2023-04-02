package com.telegrambot.friday.botApi.service.buttons;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherButtons {
    public InlineKeyboardMarkup getMessageButtons() {
        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton setCityButton = new InlineKeyboardButton();
        InlineKeyboardButton getLocationButton = new InlineKeyboardButton();

        setCityButton.setCallbackData("setNewCity");
        setCityButton.setText("Новый город\uD83D\uDDFD");

        getLocationButton.setCallbackData("getUserLocation");
        getLocationButton.setText("Геолокация\uD83D\uDCCD");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(setCityButton);
        row1.add(getLocationButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);

        replyKeyboardMarkup.setKeyboard(rowList);

        return replyKeyboardMarkup;
    }
}
