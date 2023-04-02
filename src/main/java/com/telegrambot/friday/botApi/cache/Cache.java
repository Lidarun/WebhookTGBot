package com.telegrambot.friday.botApi.cache;

import com.telegrambot.friday.botApi.config.BotState;

public interface Cache {
    void setBotState(long userID, BotState botState);
    BotState getBotState(long userID);
}
