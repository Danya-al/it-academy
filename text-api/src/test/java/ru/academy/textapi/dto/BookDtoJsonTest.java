package ru.academy.textapi.dto;

import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookDtoJsonTest {

    // Тот же Jackson 3, но созданный вручную: Spring для юнит-теста не нужен
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Test
    void roundTripKeepsAllData() {
        // Given: книга со всеми полями и автором
        AuthorDto author = new AuthorDto(1L, "Толстой", "Лев", "Николаевич", 1828, "Россия");
        BookDto original = new BookDto(1L, "Война и мир", 1300, 1869, "русский", List.of(author));

        // When: объект → JSON → снова объект
        String json = jsonMapper.writeValueAsString(original);
        BookDto restored = jsonMapper.readValue(json, BookDto.class);

        // Then: ничего не потерялось (equals от @Data сравнивает все поля, включая авторов)
        assertEquals(original, restored);
    }

    @Test
    void serializationWritesFieldValues() {
        BookDto book = new BookDto(1L, "Война и мир", 1300, 1869, "русский", List.of());

        String json = jsonMapper.writeValueAsString(book);

        assertTrue(json.contains("\"title\":\"Война и мир\""));
        assertTrue(json.contains("\"pages\":1300"));
    }

    @Test
    void nullFieldIsWrittenAsNull() {
        // language не задан → null
        BookDto book = new BookDto(1L, "Война и мир", 1300, 1869, null, List.of());

        String json = jsonMapper.writeValueAsString(book);

        // по умолчанию Jackson пишет null-поля явно
        assertTrue(json.contains("\"language\":null"));
    }

    @Test
    void missingFieldBecomesNull() {
        // в JSON нет pages и language
        String json = "{\"id\":1,\"title\":\"Война и мир\"}";

        BookDto book = jsonMapper.readValue(json, BookDto.class);

        assertEquals("Война и мир", book.getTitle());
        assertNull(book.getPages());      // Integer, а не int, поэтому может быть null
        assertNull(book.getLanguage());
    }

    @Test
    void brokenJsonThrowsJacksonException() {
        String brokenJson = "{\"title\": \"Тест\"";   // нет закрывающей }

        assertThrows(JacksonException.class, () -> jsonMapper.readValue(brokenJson, BookDto.class));
    }
}