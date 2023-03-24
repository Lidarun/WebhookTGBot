package com.telegrambot.friday.model.repository;

import com.telegrambot.friday.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    City getCityByRuNameOrEnName(String ruName, String enName);
}
