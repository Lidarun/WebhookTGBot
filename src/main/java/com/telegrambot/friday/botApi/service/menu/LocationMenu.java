package com.telegrambot.friday.botApi.service.menu;

import com.telegrambot.friday.botApi.service.MessageHolder;
import com.telegrambot.friday.botApi.state.BotState;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationMenu implements MessageHolder {
    final LocationMenuService locationMenuService;

    public LocationMenu(LocationMenuService locationMenuService) {
        this.locationMenuService = locationMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        return locationMenuService.getMenu(message.getChatId(), "Location");
    }

    @Override
    public BotState getStateHandlerName() {
        return BotState.LOCATION_MENU;
    }
}
