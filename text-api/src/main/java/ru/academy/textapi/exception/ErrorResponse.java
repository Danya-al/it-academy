package ru.academy.textapi.exception;

import java.time.LocalDateTime;

// Единый «бланк» ошибки: в таком виде клиент получает ЛЮБУЮ ошибку
public record ErrorResponse(
        int status,              // 400, 404...
        String error,            // "Bad Request", "Not Found"...
        String message,          // понятное объяснение
        LocalDateTime timestamp  // когда случилось
) {
}