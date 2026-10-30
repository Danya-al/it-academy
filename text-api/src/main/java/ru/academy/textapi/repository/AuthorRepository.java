package ru.academy.textapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.academy.textapi.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}