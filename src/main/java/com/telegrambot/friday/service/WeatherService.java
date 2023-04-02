package com.telegrambot.friday.service;

import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.Weather;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface WeatherService {
    Weather getWeatherInfo(City city);
    Weather getWeatherInfoByLocation(Message message);
}
