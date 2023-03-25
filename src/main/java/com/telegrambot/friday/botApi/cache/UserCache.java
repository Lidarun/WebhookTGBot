package com.telegrambot.friday.botApi.cache;

import com.telegrambot.friday.botApi.state.BotState;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCache implements Cache {
    Map<Long, BotState> usersBotStates = new HashMap<>();

    @Override
    public void setBotState(long userID, BotState botState) {
        usersBotStates.put(userID, botState);
    }

    @Override
    public BotState getBotState(long userID) {
        BotState botState = usersBotStates.get(userID);
        return botState;
    }
}
