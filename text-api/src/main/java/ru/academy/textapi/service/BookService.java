package ru.academy.textapi.service;

import org.springframework.stereotype.Service;
import ru.academy.textapi.entity.Book;
import ru.academy.textapi.repository.BookRepository;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService (BookRepository bookRepository) {
        this.bookRepository= bookRepository;
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }
}
