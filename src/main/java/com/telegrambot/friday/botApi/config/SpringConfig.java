package com.telegrambot.friday.botApi.config;

import com.telegrambot.friday.botApi.controller.Friday;
import com.telegrambot.friday.botApi.service.MessageHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
@AllArgsConstructor
public class SpringConfig {
    private final BotConfig config;

    @Bean
    public SetWebhook setWebhookInstance() {
        SetWebhook setWebhook = new SetWebhook();
        setWebhook.builder().url(config.getWebhookPath()).build();
        return setWebhook;
    }

    @Bean
    public Friday springFridayConfig(SetWebhook setWebhook, MessageHandler handler) {
        Friday friday = new Friday(setWebhook, handler);

        friday.setBotPath(config.getWebhookPath());
        friday.setBotToken(config.getBotToken());
        friday.setBotUsername(config.getUsername());

        return friday;
    }
}