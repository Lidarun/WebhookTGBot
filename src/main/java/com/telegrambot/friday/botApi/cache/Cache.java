package com.telegrambot.friday.botApi.cache;

import com.telegrambot.friday.botApi.state.BotState;

public interface Cache {
    void setBotState(long userID, BotState botState);
    BotState getBotState(long userID);
}
