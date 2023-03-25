package com.telegrambot.friday.botApi.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class MessageGenerator {
    public SendMessage generateMessage(long chatID, String textMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(textMessage);
        return sendMessage;
    }
}
