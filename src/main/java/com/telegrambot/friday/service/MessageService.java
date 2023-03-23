package com.telegrambot.friday.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageService {
    SendMessage send(long chatId, String message);
    SendMessage weatherInfo(long chatId);
}
