package ru.academy.textapi.chat.mapper;

import org.springframework.stereotype.Component;
import ru.academy.textapi.chat.dto.ChatMessageDto;
import ru.academy.textapi.chat.dto.MessageDto;
import ru.academy.textapi.chat.entity.Message;

import java.time.LocalDateTime;

@Component
public class MessageMapper {
    public Message ToEntity(ChatMessageDto dto, String senderIp) {
        Message message = new Message();
        message.setSenderName(dto.senderName());
        message.setContent(dto.message());
        message.setSenderIp(senderIp);
        message.setReceivedAt(LocalDateTime.now());
        return message;
    }

    public MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getSenderName(),
                message.getSenderIp(),
                message.getContent(),
                message.getReceivedAt()
        );
    }
}
