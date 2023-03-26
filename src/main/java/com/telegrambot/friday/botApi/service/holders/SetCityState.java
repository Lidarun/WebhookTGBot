package com.telegrambot.friday.botApi.service.holders;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.service.MessageGenerator;
import com.telegrambot.friday.botApi.state.BotState;
import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.Weather;
import com.telegrambot.friday.service.CityService;
import com.telegrambot.friday.service.UserService;
import com.telegrambot.friday.service.WeatherService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetCityState implements MessageHolder{
    final UserCache userCache;
    final MessageGenerator generator;
    final UserService userService;
    final WeatherService weatherService;
    final CityService cityService;

    public SetCityState(UserCache userCache, MessageGenerator generator,
                        UserService userService, WeatherService weatherService,
                        CityService cityService) {
        this.userCache = userCache;
        this.generator = generator;
        this.userService = userService;
        this.weatherService = weatherService;
        this.cityService = cityService;
    }

    @Override
    public SendMessage handle(Message message) {
        return generateMessage(message);
    }

    @Override
    public BotState getStateHandlerName() {
        return BotState.SET_CITY;
    }

    private SendMessage generateMessage(Message message) {
        long userID = message.getFrom().getId();
        long chatID = message.getChatId();

        String userMessage = message.getText();

        if (userMessage.equals("/setcity")) {
            return generator.generateMessage(chatID, "Введите название города!");

        } else {
            System.out.println(userMessage);
            City city = cityService.getCityInfo(userMessage);
            System.out.println(city.toString());

            if (city != null) {
                userService.setCity(chatID, city);
                Weather weather = weatherService.getWeatherInfo(city);

                userCache.setBotState(userID, null);
                return generator.generateMessage(chatID, "Город установлен!\n\n" + weather.toString());

            } else {
                userCache.setBotState(userID, BotState.SET_CITY);
                return generator.generateMessage(chatID, "Город не найден!");
            }
        }
    }
}
