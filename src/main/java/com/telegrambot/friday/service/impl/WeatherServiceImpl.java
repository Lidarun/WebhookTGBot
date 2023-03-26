package com.telegrambot.friday.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.Weather;
import com.telegrambot.friday.service.CityService;
import com.telegrambot.friday.service.WeatherService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherServiceImpl implements WeatherService {
    final CityService cityService;
    @Value("${openweather.weather}")
    String url;
    @Value("${openweather.token}")
    String token;
    public WeatherServiceImpl(CityService cityService) {
        this.cityService = cityService;
    }

    @Override
    public Weather getWeatherInfo(City city) {
        String urlAddress = url.replace("{lat}", String.valueOf(city.getLat()));
                urlAddress = urlAddress.replace("{lon}", String.valueOf(city.getLon()));
                urlAddress = urlAddress.replace("{API key}", token);
                urlAddress = urlAddress.replace("{lang}", "ru");

        try {
            JsonNode weatherInfo = new ObjectMapper().readTree(new URL(urlAddress));
            String json = weatherInfo.toString();

            return getWeatherFromJson(json);

        } catch (Exception e) {
            log.debug(e.getMessage() + "City: " + city);
        }

        return null;
    }

    private Weather getWeatherFromJson(String jsonWeather) {
        Weather weather = new Weather();

        weather.setWeatherMain(JsonPath.read(jsonWeather,
                        "$.weather[0].description"));
        weather.setCountry(JsonPath.read(jsonWeather, "$.sys.country"));

        weather.setCity(JsonPath.read(jsonWeather, "$.name"));
        weather.setTemp(JsonPath.read(jsonWeather, "$.main.temp"));
        weather.setWindSpeed(JsonPath.read(jsonWeather, "$.wind.speed"));

        return weather;
    }

}
