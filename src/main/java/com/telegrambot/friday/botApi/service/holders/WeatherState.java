package com.telegrambot.friday.botApi.service.holders;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.service.MessageGenerator;
import com.telegrambot.friday.botApi.state.BotState;
import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.User;
import com.telegrambot.friday.model.Weather;
import com.telegrambot.friday.service.UserService;
import com.telegrambot.friday.service.WeatherService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherState implements MessageHolder {
    WeatherService weatherService;
    UserService userService;
    MessageGenerator generator;
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

        City city = userService.getCityFromUserData(chatID);

        if (city != null) {
            Weather weather = weatherService.getWeatherInfo(city);

            if (weather != null) return generator
                    .generateMessage(chatID, weather.toString());
        }

        return generator.generateMessage(chatID, "Установите город! /setcity");
    }
}
