package com.telegrambot.friday.botApi.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotConfig {
    @Value("${telegram.webhook-path}")
    String webhookPath;
    @Value("${telegram.username}")
    String username;
    @Value("${telegram.token}")
    String botToken;
}
