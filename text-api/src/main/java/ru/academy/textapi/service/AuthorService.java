package ru.academy.textapi.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.academy.textapi.dto.AuthorDto;
import ru.academy.textapi.entity.Author;
import ru.academy.textapi.mapper.AuthorMapper;
import ru.academy.textapi.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDto> getAll() {
        return authorRepository.findAll().stream()
                .map(author -> authorMapper.toDto(author))
                .collect(Collectors.toList());
    }

    public AuthorDto getById(Long id) {
        return authorMapper.toDto(findAuthor(id));
    }

    public AuthorDto create(AuthorDto dto) {
        Author author = authorMapper.toEntity(dto);
        Author saved = authorRepository.save(author);
        return authorMapper.toDto(saved);
    }

    public AuthorDto update(Long id, AuthorDto dto) {
        Author author = findAuthor(id);
        authorMapper.updateEntity(dto, author);
        return authorMapper.toDto(authorRepository.save(author));
    }

    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор не найден: " + id);
        }
        authorRepository.deleteById(id);
    }

    private Author findAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор не найден: " + id));
    }
}