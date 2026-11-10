package ru.academy.textapi.mapper;

import org.springframework.stereotype.Component;
import ru.academy.textapi.dto.AuthorDto;
import ru.academy.textapi.dto.BookDto;
import ru.academy.textapi.dto.BookPagesDto;
import ru.academy.textapi.entity.Book;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    private final AuthorMapper authorMapper;

    public BookMapper(AuthorMapper authorMapper) {
        this.authorMapper = authorMapper;
    }

    public BookDto toDto(Book book) {

        List<AuthorDto> authors = book.getAuthors().stream()
                .map(author -> authorMapper.toDto(author))
                .collect(Collectors.toList());

        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getPages(),
                book.getPublishYear(),
                book.getLanguage(),
                authors
        );

    }

    public BookPagesDto toPagesDto(Book book) {
        return new BookPagesDto(book.getTitle(), book.getPages());
    }
}
