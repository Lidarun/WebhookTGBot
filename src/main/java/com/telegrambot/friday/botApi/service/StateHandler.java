package com.telegrambot.friday.botApi.service;

import com.telegrambot.friday.botApi.cache.BotState;
import com.telegrambot.friday.model.City;
import com.telegrambot.friday.model.User;
import com.telegrambot.friday.service.CityService;
import com.telegrambot.friday.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

//@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StateHandler {
//    final UserService userService;
//    final CityService cityService;
//
//    public StateHandler(UserService userService, CityService cityService) {
//        this.userService = userService;
//        this.cityService = cityService;
//    }
//
//    public BotApiMethod<?> replyMessage(BotState state, Message message) {
//        SendMessage sendMessage = new SendMessage();
//        long chatID = message.getChatId();
//        sendMessage.setChatId(chatID);
//
//        switch (state) {
//            case CITY_SEARCH -> {
//                City city = cityService.getCityInfo(message.getText());
//
//                if (city == null) {
//                    sendMessage.setText("Город не найден!");
//
//                } else {
//                    sendMessage.setText("Город установлен!");
//                    User user = userService.getByChatID(chatID);
//                    user.setCity(city);
//                    userService.update(user);
//
//                    MessageHandler.botState = BotState.COMMAND_NOT_SET;
//                }
//
//                return sendMessage;
//            }
//        }
//
//        return null;
//    }
}
