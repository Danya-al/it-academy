package ru.academy.textapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.academy.textapi.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
