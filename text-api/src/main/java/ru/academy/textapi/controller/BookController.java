package ru.academy.textapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.academy.textapi.dto.BookDto;
import ru.academy.textapi.dto.BookPagesDto;
import ru.academy.textapi.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/more-than-100-pages")
    public List<BookDto> getMoreThan100Pages() {
        return bookService.getBooksWithMorePages(100);
    }

    @GetMapping("/more-than")
    public List<BookDto> getMoreThan(@RequestParam int pages) {
        return bookService.getBooksWithMorePages(pages);
    }

    @GetMapping("/by-author")
    public List<BookPagesDto> getByAuthor(@RequestParam String lastName) {
        return bookService.getBooksByAuthor(lastName);
    }

    @PostMapping("/parse-json")
    public String parseJson(@RequestBody String json) {
        return bookService.parseJson(json);
    }

    @GetMapping("/as-json-string")
    public String getAllAsJsonString() {
        return bookService.getAllAsJsonString();
    }
}