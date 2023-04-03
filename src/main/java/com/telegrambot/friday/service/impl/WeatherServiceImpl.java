package com.telegrambot.friday.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.Weather;
import com.telegrambot.friday.service.CityService;
import com.telegrambot.friday.service.UserService;
import com.telegrambot.friday.service.WeatherService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.net.URL;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherServiceImpl implements WeatherService {
    final CityService cityService;
    final UserService userService;
    @Value("${openweather.weather}")
    String url;
    @Value("${openweather.token}")
    String token;

    public WeatherServiceImpl(CityService cityService, UserService userService) {
        this.cityService = cityService;
        this.userService = userService;
    }

    @Override
    public Weather getWeatherInfo(City city) {
        String urlAddress = url.replace("{lat}", String.valueOf(city.getLat()));
                urlAddress = urlAddress.replace("{lon}", String.valueOf(city.getLon()));
                urlAddress = urlAddress.replace("{API key}", token);

        try {
            JsonNode weatherInfo = new ObjectMapper().readTree(new URL(urlAddress));
            String json = weatherInfo.toString();
            return getWeatherFromJson(json);

        } catch (Exception e) {
            log.debug(e.getMessage() + "City: " + city);
        }

        return null;
    }

    @Override
    public Weather getWeatherInfoByLocation(Message message) {
        long chatID = message.getChatId();
        Location location = message.getLocation();
        String urlAddress = url.replace("{lat}", String.valueOf(location.getLatitude()));
        urlAddress = urlAddress.replace("{lon}", String.valueOf(location.getLongitude()));
        urlAddress = urlAddress.replace("{API key}", token);

        try {
            JsonNode weatherInfo = new ObjectMapper().readTree(new URL(urlAddress));
            String json = weatherInfo.toString();
            Weather weather = getWeatherFromJson(json);

            //Устанавлиеваем стандарный город для юзера
            userService.setCity(chatID, cityService.getCityInfo(weather.getCity()));

            return weather;

        } catch (Exception e) {
            log.debug(e.getMessage() + "Location: " + location);
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

            String windSpeed = JsonPath.read(jsonWeather, "$.wind.speed").toString();

            weather.setWindSpeed(Double.parseDouble(windSpeed));

            return generateEmoji(weather);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private Weather generateEmoji(Weather weather) {
        String weatherDescription = weather.getWeatherMain().toLowerCase();
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
            case "небольшой проливаной дождь":
                emoji = "\uD83C\uDF26";
                break;

            case "дождь":
                emoji = "\uD83C\uDF27";
                break;
            case "небольшой снег":
            case "снег":
                emoji = "\uD83C\uDF28";
                break;

            case "дымка":
                emoji = "\uD83C\uDF2B";
                break;
            default:
                emoji = "\uD83E\uDE90";
        }

        weather.setEmoji(emoji);
        return weather;
    }
}
