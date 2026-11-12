package ru.academy.textapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.academy.textapi.dto.AuthorDto;
import ru.academy.textapi.entity.Author;

// componentModel = "spring": сгенерированный класс станет бином, как раньше @Component
@Mapper(componentModel = "spring")
public interface AuthorMapper {

    // Author → AuthorDto: все поля совпадают по именам, MapStruct сопоставит сам
    AuthorDto toDto(Author author);

    // AuthorDto → новый Author: id не переносим, его выдаёт база
    @Mapping(target = "id", ignore = true)
    Author toEntity(AuthorDto dto);

    // AuthorDto → в СУЩЕСТВУЮЩИЙ Author (для PUT): @MappingTarget = «заполни этот объект»
    @Mapping(target = "id", ignore = true)
    void updateEntity(AuthorDto dto, @MappingTarget Author author);
}




//package ru.academy.textapi.mapper;
//
//import org.springframework.stereotype.Component;
//import ru.academy.textapi.dto.AuthorDto;
//import ru.academy.textapi.entity.Author;
//
//@Component
//public class AuthorMapper {
//    public AuthorDto toDto(Author author) {
//
//        return new AuthorDto(
//                author.getId(),
//                author.getLastName(),
//                author.getFirstName(),
//                author.getMiddleName(),
//                author.getBirthYear(),
//                author.getCountry()
//        );
//    }
//
//    public Author toEntity(AuthorDto dto) {
//        Author author = new Author();
//        updateEntity(dto, author);
//        return author;
//    }
//
//    public void updateEntity(AuthorDto dto, Author author) {
//        author.setLastName(dto.getLastName());
//        author.setFirstName(dto.getFirstName());
//        author.setMiddleName(dto.getMiddleName());
//        author.setBirthYear(dto.getBirthYear());
//        author.setCountry(dto.getCountry());
//    }
//}
