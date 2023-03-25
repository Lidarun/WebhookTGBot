package com.telegrambot.friday.service;


import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.Weather;

public interface WeatherService {
    Weather getWeatherInfo(City city);
}
