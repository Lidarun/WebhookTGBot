package com.telegrambot.friday.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.Weather;
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
    @Value("${openweather.weather}")
    String url;
    @Value("${openweather.token}")
    String token;

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

        try {
            weather.setWeatherMain(JsonPath.read(jsonWeather,
                    "$.weather[0].description"));
            weather.setCountry(JsonPath.read(jsonWeather, "$.sys.country"));
            weather.setCity(JsonPath.read(jsonWeather, "$.name"));
            weather.setTemp(JsonPath.read(jsonWeather, "$.main.temp"));

            if (JsonPath.read(jsonWeather, "$.wind.speed") instanceof Integer) {
                int speed = JsonPath.read(jsonWeather, "$.wind.speed");
                weather.setWindSpeed(speed);
            }else {
                weather.setWindSpeed(JsonPath.read(jsonWeather, "$.wind.speed"));
            }

            return generateEmoji(weather);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private Weather generateEmoji(Weather weather) {
        String weatherDescription = weather.getWeatherMain().toLowerCase();
        System.out.println(weatherDescription + " " + weatherDescription.length());
        String emoji;
        switch (weatherDescription) {
            case "ясно":
                emoji = "☀️";
                break;

            case "небольшая облачность":
            case "переменная облачность":
            case "облачно с прояснениями":
                emoji = "⛅️";
                break;

            case "облачно":
            case "пасмурно":
                emoji = "☁️";
                break;

            case "небольшой дождь":
                emoji = "\uD83C\uDF26";
                break;
            case "дождь":
                emoji = "\uD83C\uDF27";
                break;
            case "снег":
                emoji = "\uD83C\uDF28";
                break;
            default:
                emoji = "\uD83E\uDE90";
        }

        weather.setEmoji(emoji);
        return weather;
    }
}
