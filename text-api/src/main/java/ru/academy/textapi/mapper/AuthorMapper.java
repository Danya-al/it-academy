package ru.academy.textapi.mapper;

import org.springframework.stereotype.Component;
import ru.academy.textapi.dto.AuthorDto;
import ru.academy.textapi.entity.Author;

@Component
public class AuthorMapper {
    public AuthorDto toDto(Author author) {

        return new AuthorDto(
                author.getId(),
                author.getLastName(),
                author.getFirstName(),
                author.getMiddleName(),
                author.getBirthYear(),
                author.getCountry()
        );
    }

    public Author toEntity(AuthorDto dto) {
        Author author = new Author();
        updateEntity(dto, author);
        return author;
    }

    public void updateEntity(AuthorDto dto, Author author) {
        author.setLastName(dto.getLastName());
        author.setFirstName(dto.getFirstName());
        author.setMiddleName(dto.getMiddleName());
        author.setBirthYear(dto.getBirthYear());
        author.setCountry(dto.getCountry());
    }
}
