package ru.academy.textapi.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.academy.textapi.chat.dto.ChatMessageDto;
import ru.academy.textapi.chat.dto.MessageDto;
import ru.academy.textapi.chat.dto.SendMessageRequest;
import ru.academy.textapi.chat.entity.Message;
import ru.academy.textapi.chat.mapper.MessageMapper;
import ru.academy.textapi.chat.repository.MessageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final RestTemplate restTemplate;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public ChatService(RestTemplate restTemplate,
                       MessageRepository messageRepository,
                       MessageMapper messageMapper) {
        this.restTemplate = restTemplate;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    // 1. Отправить сообщение на другой сервер
    public void sendMessage(SendMessageRequest request) {
        String url = "http://" + request.ip() + "/api/chat/receive-message";
        ChatMessageDto dto = new ChatMessageDto(request.senderName(), request.message());
        restTemplate.postForObject(url, dto, Void.class);
        log.info("Отправлено на {}: {}", request.ip(), request.message());
    }

    // 2. Принять сообщение от другого сервера и сохранить в базу
    public void receiveMessage(ChatMessageDto dto, String senderIp) {
        log.info("Сообщение от {} ({}): {}", dto.senderName(), senderIp, dto.message());
        Message message = messageMapper.toEntity(dto, senderIp);
        messageRepository.save(message);
    }

    // 3. Все сохранённые сообщения
    public List<MessageDto> readAllMessages() {
        return messageRepository.findAll().stream()
                .map(m -> messageMapper.toDto(m))
                .collect(Collectors.toList());
    }

    // 4. Сообщения от конкретного отправителя: по имени или по IP
    public List<MessageDto> readAllMessagesFrom(String senderName, String senderIp) {
        List<Message> messages;
        if (senderName != null) {
            messages = messageRepository.findBySenderName(senderName);
        } else {
            messages = messageRepository.findBySenderIp(senderIp);
        }
        return messages.stream()
                .map(m -> messageMapper.toDto(m))
                .collect(Collectors.toList());
    }
}
