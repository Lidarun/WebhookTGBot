package com.telegrambot.friday.botApi.service;

import com.telegrambot.friday.botApi.state.BotState;
import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.User;
import com.telegrambot.friday.model.Weather;
import com.telegrambot.friday.service.CityService;
import com.telegrambot.friday.service.UserService;
import com.telegrambot.friday.service.WeatherService;
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
    final UserService userService;
    final WeatherService weatherService;
    final CityService cityService;

    public StateHandler(UserService userService, WeatherService weatherService, CityService cityService) {
        this.userService = userService;
        this.weatherService = weatherService;
        this.cityService = cityService;
    }

    public BotApiMethod<?> replyMessage(BotState botState, Message message) {
        SendMessage sendMessage = new SendMessage();
        String userMessage = message.getText();
        long chatID = message.getChatId();
        sendMessage.setChatId(chatID);

        switch (botState) {
            case START -> {
                userService.save(message);
                sendMessage.setText("Приветствую, " + message.getChat().getFirstName() + "!");
                return sendMessage;
            }
            case WEATHER -> {
                Weather weather = weatherService.getWeatherInfo(userMessage);

                if (weather != null) {
                    sendMessage.setText(weather.toString());
                }else {
                    sendMessage.setText("Установите город! /setcity");
                }

                return sendMessage;
            }

            case SET_CITY -> {
                if (userMessage.equals("/setcity")) {
                    sendMessage.setText("Введите название города!");
                    return sendMessage;
                }

                City city = cityService.getCityInfo(userMessage);

                if (city != null) {
                    User user = userService.getUserByChatID(chatID);
                    user.setCity(city);
                    userService.update(user);
                    Weather weather = weatherService.getWeatherInfo(city.getEnName());
                    sendMessage.setText("Город установлен! \nПогода в вашем городе: \n" + weather.toString());

                } else {
                    sendMessage.setText("Город не найден!");
                }
                return sendMessage;
            }

            case COMMAND_NOT_SET -> {
                sendMessage.setText("Команда не поддерживается");
                return sendMessage;
            }
        }

        return null;
    }
}
