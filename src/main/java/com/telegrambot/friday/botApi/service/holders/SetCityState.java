package com.telegrambot.friday.botApi.service.holders;

import com.telegrambot.friday.botApi.cache.UserCache;
import com.telegrambot.friday.botApi.service.MessageGenerator;
import com.telegrambot.friday.botApi.config.BotState;
import com.telegrambot.friday.botApi.service.buttons.WeatherButtons;
import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.User;
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
public class SetCityState implements MessageHolder {
    final UserCache userCache;
    final MessageGenerator generator;
    final UserService userService;
    final WeatherService weatherService;
    final CityService cityService;
    final WeatherButtons buttons;

    public SetCityState(UserCache userCache, MessageGenerator generator,
                        UserService userService, WeatherService weatherService,
                        CityService cityService, WeatherButtons buttons) {
        this.userCache = userCache;
        this.generator = generator;
        this.userService = userService;
        this.weatherService = weatherService;
        this.cityService = cityService;
        this.buttons = buttons;
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
        userCache.setBotState(userID, null);
        String userMessage = message.getText();

        if (userMessage.equals("/setcity")) {
            return generator.generateMessage(chatID, "Введите название города!");

        } else {
            City city = cityService.getCityInfo(userMessage);

            if (city != null) {

                Weather weather = weatherService.getWeatherInfo(city);
                User user = userService.getUserByChatID(chatID);

                if (user == null) return generator.generateMessage(chatID, "Чтобы я мог установить стандартный город для вас," +
                        " пожалуйста воспользуйтесь командой /start. \nПогода по вашему запросу:\n" + weather.toString());

                userService.setCity(chatID, city);

                SendMessage messageToUser = generator
                        .generateMessage(chatID, "Город установлен!\n\n" + weather.toString());
                messageToUser.setReplyMarkup(buttons.getMessageButtons());
                return messageToUser;
            }
        }

        return generator.generateMessage(chatID, "Извините, город не найден! " +
                "\nПроверьте правильность написанися города и повторите попытку");
    }
}
