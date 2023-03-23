package com.telegrambot.friday.service;

import com.telegrambot.friday.model.City;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserService {
    void save(Update update);
    City getCityFromUserData(long chatId);
}
