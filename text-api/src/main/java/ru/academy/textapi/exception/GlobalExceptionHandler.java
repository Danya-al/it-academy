package ru.academy.textapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.core.JacksonException;

import java.time.LocalDateTime;

@RestControllerAdvice   // «перехватчик ошибок для ВСЕХ контроллеров приложения»
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Кривой JSON в @RequestBody (например, POST /api/authors) → 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("Некорректный JSON в теле запроса: {}", e.getMessage());
        return build(HttpStatus.BAD_REQUEST, "Некорректный JSON в теле запроса");
    }

    // 2. Кривой JSON, который мы разбираем САМИ через JsonMapper (POST /api/books/parse-json) → 400
    @ExceptionHandler(JacksonException.class)
    public ResponseEntity<ErrorResponse> handleJackson(JacksonException e) {
        log.warn("Не удалось разобрать JSON: {}", e.getMessage());
        return build(HttpStatus.BAD_REQUEST, "Не удалось разобрать JSON");
    }

    // 3. Наши ResponseStatusException («Автор не найден: 7») → тот же единый формат
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleStatus(ResponseStatusException e) {
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
        return build(status, e.getReason());
    }

    // 4. Параметр не того типа: /api/books/more-than?pages=abc → 400
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = "Параметр '" + e.getName() + "' имеет неверное значение: " + e.getValue();
        return build(HttpStatus.BAD_REQUEST, message);
    }

    // Помощник: собрать ответ с нужным статусом и телом ErrorResponse
    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        ErrorResponse body = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(body);
    }
}