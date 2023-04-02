package com.telegrambot.friday.botApi.service.holders;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.config.BotState;
import com.telegrambot.friday.botApi.service.MessageGenerator;
import com.telegrambot.friday.botApi.service.buttons.WeatherButtons;
import com.telegrambot.friday.model.Weather;
import com.telegrambot.friday.service.WeatherService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherByLocationState implements MessageHolder {
    final WeatherService weatherService;
    final MessageGenerator generator;
    final WeatherButtons buttons;
    UserCache userCache;

    public WeatherByLocationState(WeatherService weatherService, MessageGenerator generator, WeatherButtons buttons, UserCache userCache) {
        this.weatherService = weatherService;
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
        return BotState.WEATHER_BY_LOCATION;
    }


    private SendMessage generateMessage(Message message) {
        long userID = message.getFrom().getId();
        long chatID = message.getChatId();
        InlineKeyboardMarkup replyKeyboardMarkup = buttons.getMessageButtons();
        Weather weather = weatherService.getWeatherInfoByLocation(message);
        userCache.setBotState(userID, null);
        SendMessage msgToUser;


        if (weather == null) {
            msgToUser = generator
                    .generateMessage(chatID, "Извините, данных по вашей геолокации не найдено! " +
                            "Попробуйте установаить по названию вашего города");
            msgToUser.setReplyMarkup(replyKeyboardMarkup);

            return msgToUser;
        }

        msgToUser = generator
                .generateMessage(chatID, "Город установлен!\n\n" + weather.toString());
        msgToUser.setReplyMarkup(replyKeyboardMarkup);

        return msgToUser;
    }
}
