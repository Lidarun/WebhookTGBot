package com.telegrambot.friday.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.Weather;
import com.telegrambot.friday.service.CityService;
import com.telegrambot.friday.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;

@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {
    private final CityService cityService;
    private String url = "https://api.openweathermap.org/data/2.5/weather?" +
            "lat={lat}&lon={lon}&appid={API key}&lang={lang}";

    public WeatherServiceImpl(CityService cityService) {
        this.cityService = cityService;
    }

    @Override
    public Weather getWeatherInfo(String city) {
        City cityInfo = cityService.getCityInfo(city);

        url = url.replace("{lat}", String.valueOf(cityInfo.getLat()));
        url = url.replace("{lon}", String.valueOf(cityInfo.getLon()));
        url = url.replace("{API key}",
                "097e7a36c584cdb3b0001619654e57d1");
        url = url.replace("{lang}", "ru");

        try {
            JsonNode weatherInfo = new ObjectMapper().readTree(new URL(url));
            String json = weatherInfo.toString();
            return getWeatherFromJson(json);

        } catch(Exception e) {
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
