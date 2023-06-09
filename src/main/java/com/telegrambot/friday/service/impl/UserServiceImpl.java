package com.telegrambot.friday.service.impl;

import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.User;
import com.telegrambot.friday.model.repository.UserRepository;
import com.telegrambot.friday.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Message message) {
        User userFromDB = repository.getUserByChatId(message.getChatId());

        if (userFromDB == null) {
            User user = new User();
            user.setChatId(message.getChatId());
            user.setFName(message.getChat().getFirstName());
            user.setLName(message.getChat().getLastName());
            user.setUName(message.getChat().getUserName());
            user.setRegisterDate(new Date(System.currentTimeMillis()));
            user.setActive(true);

            repository.save(user);
        }
    }

    @Override
    public void setCity(long chatID, City city) {
        User user = repository.getUserByChatId(chatID);
        user.setCity(city);
        repository.save(user);
    }

    @Override
    public User getUserByChatID(long chatID) {
        return Optional.ofNullable(repository.getUserByChatId(chatID)).orElse(null);
    }

    @Override
    public City getCityFromUserData(long chatId) {
        User user = repository.getUserByChatId(chatId);
        if (user != null) return user.getCity();

        return null;
    }
}
