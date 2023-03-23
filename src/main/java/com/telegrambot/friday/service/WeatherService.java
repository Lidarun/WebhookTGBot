package com.telegrambot.friday.service;


import com.telegrambot.friday.model.Weather;

public interface WeatherService {
    Weather getWeatherInfo(String city);
}
