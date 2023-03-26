package com.telegrambot.friday.service;

import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface UserService {
    void save(Message message);
    void setCity(long chatID, City city);
    User getUserByChatID(long chatID);
    City getCityFromUserData(long chatId);
}
