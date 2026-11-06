package ru.academy.textapi.chat.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.academy.textapi.chat.dto.ChatMessageDto;
import ru.academy.textapi.chat.dto.MessageDto;
import ru.academy.textapi.chat.dto.SendMessageRequest;
import ru.academy.textapi.chat.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 1. Ты из Postman → свой сервер: «отправь сообщение»
    @PostMapping("/send-message")
    public void sendMessage(@RequestBody SendMessageRequest request) {
        chatService.sendMessage(request);
    }

    // 2. Другой сервер → твой сервер: «вот тебе сообщение»
    @PostMapping("/receive-message")
    public void receiveMessage(@RequestBody ChatMessageDto dto, HttpServletRequest request) {
        chatService.receiveMessage(dto, request.getRemoteAddr());
    }

    // 3. Все сообщения
    @GetMapping("/messages")
    public List<MessageDto> readAllMessages() {
        return chatService.readAllMessages();
    }

    // 4. Сообщения от кого-то: /messages/from?name=Daniil или /messages/from?ip=127.0.0.1
    @GetMapping("/messages/from")
    public List<MessageDto> readAllMessagesFrom(@RequestParam(required = false) String name,
                                                @RequestParam(required = false) String ip) {
        return chatService.readAllMessagesFrom(name, ip);
    }
}
