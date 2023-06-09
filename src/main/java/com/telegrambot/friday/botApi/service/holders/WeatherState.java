package com.telegrambot.friday.botApi.service.holders;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.service.MessageGenerator;
import com.telegrambot.friday.botApi.config.BotState;
import com.telegrambot.friday.botApi.service.buttons.WeatherButtons;
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

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherState implements MessageHolder {
    final WeatherService weatherService;
    final UserService userService;
    final MessageGenerator generator;
    final WeatherButtons buttons;
    UserCache userCache;

    public WeatherState(WeatherService weatherService, UserService userService,
                        MessageGenerator generator, WeatherButtons buttons, UserCache userCache) {
        this.weatherService = weatherService;
        this.userService = userService;
        this.generator = generator;
        this.buttons = buttons;
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
        InlineKeyboardMarkup replyKeyboardMarkup = buttons.getMessageButtons();
        City city = userService.getCityFromUserData(chatID);
        userCache.setBotState(userID, null);
        SendMessage msgToUser;


        if (city != null) {
            Weather weather = weatherService.getWeatherInfo(city);

            if (weather == null) {
                msgToUser = generator
                        .generateMessage(chatID, "Извините, данных по вашему городу не найдено!");
                msgToUser.setReplyMarkup(replyKeyboardMarkup);

                return msgToUser;
            }

            msgToUser = generator
                    .generateMessage(chatID, String.valueOf(weather));
            msgToUser.setReplyMarkup(replyKeyboardMarkup);
            return msgToUser;
        }

        msgToUser = generator.generateMessage(chatID, "У вас не установлен город!");
        msgToUser.setReplyMarkup(replyKeyboardMarkup);
        return msgToUser;
    }
}
