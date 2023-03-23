package com.telegrambot.friday.botApi.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.client.RestTemplate;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TelegramApiClient {
    String URL;
    String fridayToken;
    RestTemplate template;

    public TelegramApiClient(String url, String fridayToken, RestTemplate template) {
        URL = url;
        this.fridayToken = fridayToken;
        this.template = template;
    }
}
