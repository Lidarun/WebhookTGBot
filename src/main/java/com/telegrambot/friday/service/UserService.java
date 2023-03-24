package com.telegrambot.friday.service;

import com.telegrambot.friday.model.City;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface UserService {
    void save(Message message);
    City getCityFromUserData(long chatId);
}
