package ru.academy.textapi.service;

import org.springframework.stereotype.Service;
import ru.academy.textapi.dto.BookPagesDto;
import ru.academy.textapi.entity.Book;
import ru.academy.textapi.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksWithMorePages(int minPages) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getPages() != null && book.getPages() > minPages)
                .collect(Collectors.toList());
    }

    public List<BookPagesDto> getBooksByAuthor(String lastName) {
        return bookRepository.findDistinctByAuthors_LastName(lastName).stream()
                .map(book -> new BookPagesDto(book.getTitle(), book.getPages()))
                .collect(Collectors.toList());
    }
}