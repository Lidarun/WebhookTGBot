package com.telegrambot.friday.service.impl;

import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.User;
import com.telegrambot.friday.model.repository.UserRepository;
import com.telegrambot.friday.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Date;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Update update) {
        User user = new User();
        user.setChatId(update.getMessage().getChatId());
        user.setFName(update.getMessage().getChat().getFirstName());
        user.setLName(update.getMessage().getChat().getLastName());
        user.setUName(update.getMessage().getChat().getUserName());
        user.setRegisterDate(new Date(System.currentTimeMillis()));
        user.setActive(false);

        repository.save(user);
    }

    @Override
    public City getCityFromUserData(long chatId) {
        User user = repository.getUserByChatId(chatId);

        City city = user.getCity();
        if (city != null) return city;

        return null;
    }
}
