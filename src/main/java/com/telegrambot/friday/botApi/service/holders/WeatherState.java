package com.telegrambot.friday.botApi.service.holders;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.service.MessageGenerator;
import com.telegrambot.friday.botApi.state.BotState;
import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.Weather;
import com.telegrambot.friday.service.UserService;
import com.telegrambot.friday.service.WeatherService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherState implements MessageHolder {
    final WeatherService weatherService;
    final UserService userService;
    final MessageGenerator generator;
    UserCache userCache;

    public WeatherState(WeatherService weatherService, UserService userService,
                        MessageGenerator generator, UserCache userCache) {
        this.weatherService = weatherService;
        this.userService = userService;
        this.generator = generator;
        this.userCache = userCache;
    }

    @Override
    public SendMessage handle(Message message) {
        return generateMessage(message);
    }

    @Override
    public BotState getStateHandlerName() {
        return BotState.WEATHER;
    }

    private SendMessage generateMessage(Message message) {
        long userID = message.getFrom().getId();
        long chatID = message.getChatId();
        userCache.setBotState(userID, null);

        SendMessage msgToUser;

        City city = userService.getCityFromUserData(chatID);

        if (city != null) {
            Weather weather = weatherService.getWeatherInfo(city);

            if (weather == null) {
                msgToUser = generator
                        .generateMessage(chatID, "Извините, данных по вашему городу не найдено!");
                msgToUser.setReplyMarkup(getMessageButtons());

                return msgToUser;
            }

            msgToUser = generator
                    .generateMessage(chatID, String.valueOf(weather));
            msgToUser.setReplyMarkup(getMessageButtons());
            return msgToUser;
        }

        msgToUser = generator.generateMessage(chatID, "Установите город! /setcity");
        msgToUser.setReplyMarkup(getMessageButtons());
        return msgToUser;
    }

    private InlineKeyboardMarkup getMessageButtons() {
        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton setCityButton = new InlineKeyboardButton();
        InlineKeyboardButton getLocationButton = new InlineKeyboardButton();

        setCityButton.setCallbackData("setNewCity");
        setCityButton.setText("Установить новый город!");

        getLocationButton.setCallbackData("getUserLocation");
        getLocationButton.setText("Определить по геолокации!");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(setCityButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(getLocationButton);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);

        replyKeyboardMarkup.setKeyboard(rowList);

        return replyKeyboardMarkup;
    }
}
