package com.telegrambot.friday.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@Data
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Weather {
    String city;
    String country;
    String emoji;
    String weatherMain;
    double temp;
    double windSpeed;

    @Override
    public String toString() {
        Locale locale = new Locale("ru", "RU");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());

        weatherMain = weatherMain.substring(0, 1).toUpperCase() + weatherMain.substring(1);

        return " \uD83D\uDDFA  " + city + "("+country+")" + " " +
                "  \uD83D\uDDD3 " + date +
                "\n " + emoji + weatherMain  +
                "\n \uD83C\uDF21 " + String.format("%.1f",(temp - 273.15)) + "°C    " +
                "   \uD83C\uDF2C " +  String.format("%.1f", windSpeed) + " м/с";
    }
}
