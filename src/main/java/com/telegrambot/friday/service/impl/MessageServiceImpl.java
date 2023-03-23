package com.telegrambot.friday.service.impl;

import com.telegrambot.friday.model.City;
import com.telegrambot.friday.service.MessageService;
import com.telegrambot.friday.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class MessageServiceImpl implements MessageService {
    private final UserService userService;

    public MessageServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public SendMessage send(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message);
        return sendMessage;
    }

    @Override
    public SendMessage weatherInfo(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        City city = userService.getCityFromUserData(chatId);

        if(city == null) {
            sendMessage.setText("Установите город! \n/setCity");
        } else sendMessage.setText("Погода");

        return sendMessage;
    }
}
