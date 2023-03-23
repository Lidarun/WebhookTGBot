package com.telegrambot.friday.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "cities_info")
public class City {
    @Id
    @Column(name = "city_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    String country;
    double lat;
    double lon;

    @Override
    public String toString() {
        return "Город: "+ name +
                "\nСтрана: " + country + '\'' +
                "\nДолгота: " + lat +
                "\nШирота: " + lon;
    }
}
