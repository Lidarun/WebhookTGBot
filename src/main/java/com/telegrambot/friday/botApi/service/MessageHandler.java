package com.telegrambot.friday.botApi.service;

import com.telegrambot.friday.model.Weather;
import com.telegrambot.friday.service.CityService;
import com.telegrambot.friday.service.WeatherService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  MessageHandler {

    WeatherService weatherService;
    CityService cityService;

    public MessageHandler( WeatherService weatherService, CityService cityService) {
        this.weatherService = weatherService;
        this.cityService = cityService;
    }

    public BotApiMethod<?> replyMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        String userMessage = message.getText();
        sendMessage.setChatId(message.getChatId());


        switch (userMessage) {
            case "/start":
                sendMessage.setText("Привет, " + message.getChat().getFirstName() + "!");
                return sendMessage;

            case "/weather":
                Weather weather = weatherService.getWeatherInfo("London");
                sendMessage.setText(weather.toString());
                return sendMessage;

            case "setCity":
                sendMessage.setText("Введите название вашего города!");
                return sendMessage;

            case "/nur":
                sendMessage.setText("Бот написан на языке Java, " +
                        "с ипользованием фреймворка Spring. Создатель @lidarunium");
                return sendMessage;

            default:
                sendMessage.setText("Неизвестная команда!");
                return sendMessage;

        }
    }
}
