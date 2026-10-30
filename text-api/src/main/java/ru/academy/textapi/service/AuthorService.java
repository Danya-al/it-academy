package ru.academy.textapi.service;

import org.springframework.stereotype.Service;
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
}