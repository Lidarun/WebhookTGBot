package com.telegrambot.friday.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "tb_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    long chatId;
    String fName;
    String lName;
    String uName;

    @ManyToOne
    @JoinColumn(name = "city_id")
    City city;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    Date registerDate;
    boolean active;

    @Override
    public String toString() {
        return  "\nИмя: " + String.format("%14s %20s",fName, (lName == null ? " ": lName)) +
                "\nНикнейм: " + String.format("%12s",uName) +
                "\nДобавлен: " + String.format("%11S",registerDate) +
                "city" + city;
    }
}
