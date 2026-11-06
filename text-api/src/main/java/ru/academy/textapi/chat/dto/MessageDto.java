package ru.academy.textapi.chat.dto;

import java.time.LocalDateTime;

public record MessageDto(Long id, String senderName, String senderIp, String message, LocalDateTime receivedAt) {
}
