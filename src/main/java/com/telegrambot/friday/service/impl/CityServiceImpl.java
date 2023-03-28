package com.telegrambot.friday.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.repository.CityRepository;
import com.telegrambot.friday.service.CityService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;

@Log4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CityServiceImpl implements CityService {
    final CityRepository cityRepository;
    @Value("${openweather.city}")
    String url;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override//Поиск города
    public City getCityInfo(String cityName) {
        City city = getCityFromDB(cityName);

        if(city == null) {
            String urlAddress = url.replace("{city}", cityName);

            try {
                JsonNode cityInfo = new ObjectMapper().readTree(new URL(urlAddress));
                String json = cityInfo.toString();
                json = json.substring(1, json.length() - 1);
                city = getCityFromJson(json);

                return city;

            } catch (Exception e) {
                log.debug(e.getMessage() + "City: " + cityName);
            }
        }
        return city;
    }

    //Извлекаем данные города из JSON
    private City getCityFromJson(String jsonCity) {
        JSONObject object = new JSONObject(jsonCity);
        City city = new City();

        city.setRuName(JsonPath.read(jsonCity, "$.local_names.ru"));
        city.setEnName(JsonPath.read(jsonCity, "name"));
        city.setCountry(object.getString("country"));
        city.setLat(object.getDouble("lat"));
        city.setLon(object.getDouble("lon"));

        saveWithCheck(city);

        return city;
    }

    //Поиск города по названию из базы
    private City getCityFromDB(String city) {
        String city2 = city.replace("-", " ");
        City cityFromDB = cityRepository
                .getCityByRuNameContainsIgnoreCaseOrEnNameContainsIgnoreCase(city, city);
        if (cityFromDB == null) cityFromDB = cityRepository
                .getCityByRuNameContainsIgnoreCaseOrEnNameContainsIgnoreCase(city2, city2);

        return cityFromDB;
    }

    private void saveWithCheck(City city) {
        City cityFromDB = cityRepository
                .getCityByRuNameContainsIgnoreCaseOrEnNameContainsIgnoreCase(city.getRuName(), city.getEnName());
        if (cityFromDB == null) cityRepository.save(city);
    }
}
