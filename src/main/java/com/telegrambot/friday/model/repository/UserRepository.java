package com.telegrambot.friday.model.repository;

import com.telegrambot.friday.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByChatId(long chatId);
}
