package ru.academy.textapi.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.academy.textapi.chat.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderName(String senderName);
    List<Message> findBySenderIp(String senderIp);
}
