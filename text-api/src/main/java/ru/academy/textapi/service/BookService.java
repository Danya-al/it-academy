package ru.academy.textapi.service;

import org.springframework.stereotype.Service;
import ru.academy.textapi.dto.BookDto;
import ru.academy.textapi.dto.BookPagesDto;
import ru.academy.textapi.mapper.BookMapper;
import ru.academy.textapi.repository.BookRepository;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final JsonMapper jsonMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, JsonMapper jsonMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.jsonMapper = jsonMapper;
    }

    public List<BookDto> getAll() {
        return bookRepository.findAll().stream()
                .map(book -> bookMapper.toDto(book))
                .collect(Collectors.toList());
    }

    public List<BookDto> getBooksWithMorePages(int minPages) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getPages() != null && book.getPages() > minPages)
                .map(book -> bookMapper.toDto(book))
                .collect(Collectors.toList());
    }

    public List<BookPagesDto> getBooksByAuthor(String lastName) {
        return bookRepository.findDistinctByAuthors_LastName(lastName).stream()
                .map(book -> bookMapper.toPagesDto(book))
                .collect(Collectors.toList());
    }

    // Задание 2.1: JSON-строка → BookDto вручную
    public String parseJson(String json) {
        BookDto dto = jsonMapper.readValue(json, BookDto.class);
        return "Получен объект: " + dto;
    }

    // Задание 2.2: List<BookDto> → JSON-строка вручную
    public String getAllAsJsonString() {
        List<BookDto> books = getAll();
        return jsonMapper.writeValueAsString(books);
    }
}