package ru.academy.textapi.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.academy.textapi.entity.Author;
import ru.academy.textapi.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    public Author getById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор не найден: " + id));
    }

    public Author create(Author author) {
        author.setId(null);
        return authorRepository.save(author);
    }

    public Author update(Long id, Author newData) {
        Author author = getById(id);
        author.setLastName(newData.getLastName());
        author.setFirstName(newData.getFirstName());
        author.setMiddleName(newData.getMiddleName());
        author.setBirthYear(newData.getBirthYear());
        author.setCountry(newData.getCountry());
        return authorRepository.save(author);
    }

    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор не найден: " + id);
        }
        authorRepository.deleteById(id);
    }
}