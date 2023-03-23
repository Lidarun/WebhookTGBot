package com.telegrambot.friday.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.repository.CityRepository;
import com.telegrambot.friday.service.CityService;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URL;

@Log4j
@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private String urlAddress = "https://api.openweathermap.org/geo/1.0/direct?q={city}" +
            "&limit=5&appid=097e7a36c584cdb3b0001619654e57d1";

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    @Override//Поиск города
    public City getCityInfo(String cityName) {
        City city = getCityFromDB(cityName);
        if(city != null) return city;

        urlAddress = urlAddress.replace("{city}", cityName);

        try {
            JsonNode cityInfo = new ObjectMapper().readTree(new URL(urlAddress));
            String json = cityInfo.toString();
            json = json.substring(1, json.length() - 1);
            city = getCityFromJson(json);
            return city;

        } catch(Exception e) {
            log.debug(e.getMessage() + "City: " + cityName);
        }

        return null;
    }

    //Извлекаем данные города из JSON
    private City getCityFromJson(String jsonCity) {
        JSONObject object = new JSONObject(jsonCity);
        City city = new City();

        city.setName(JsonPath.read(jsonCity, "$.local_names.ru"));
        city.setCountry(object.getString("country"));
        city.setLat(object.getDouble("lat"));
        city.setLon(object.getDouble("lon"));

        cityRepository.save(city);
        return city;
    }

    //Поиск города по названию из базы
    private City getCityFromDB(String city) {
        return cityRepository.getCityByName(city);
    }
}
