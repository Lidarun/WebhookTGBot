package com.telegrambot.friday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.starter.TelegramBotInitializer;

@SpringBootApplication
public class FridayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FridayApplication.class, args);
	}

}
