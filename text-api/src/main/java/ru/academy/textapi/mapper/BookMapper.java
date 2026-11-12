package ru.academy.textapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.academy.textapi.dto.BookDto;
import ru.academy.textapi.dto.BookPagesDto;
import ru.academy.textapi.entity.Book;

// uses = AuthorMapper.class: для авторов внутри книги используй AuthorMapper
@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public interface BookMapper {

    // Book → BookDto. Set<Author> → List<AuthorDto> MapStruct сделает сам через AuthorMapper.toDto
    BookDto toDto(Book book);

    // Book → BookPagesDto (record): возьмёт title и pages
    BookPagesDto toPagesDto(Book book);

    // BookDto → Book: «и обратно» из задания. Пока не используется,
    // пригодится, если сделаем POST для книг
    @Mapping(target = "id", ignore = true)
    Book toEntity(BookDto dto);
}


//package ru.academy.textapi.mapper;
//
//import org.springframework.stereotype.Component;
//import ru.academy.textapi.dto.AuthorDto;
//import ru.academy.textapi.dto.BookDto;
//import ru.academy.textapi.dto.BookPagesDto;
//import ru.academy.textapi.entity.Book;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class BookMapper {
//
//    private final AuthorMapper authorMapper;
//
//    public BookMapper(AuthorMapper authorMapper) {
//        this.authorMapper = authorMapper;
//    }
//
//    public BookDto toDto(Book book) {
//
//        List<AuthorDto> authors = book.getAuthors().stream()
//                .map(author -> authorMapper.toDto(author))
//                .collect(Collectors.toList());
//
//        return new BookDto(
//                book.getId(),
//                book.getTitle(),
//                book.getPages(),
//                book.getPublishYear(),
//                book.getLanguage(),
//                authors
//        );
//
//    }
//
//    public BookPagesDto toPagesDto(Book book) {
//        return new BookPagesDto(book.getTitle(), book.getPages());
//    }
//}
