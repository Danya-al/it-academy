package ru.academy.textapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.academy.textapi.entity.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findDistinctByAuthors_LastName(String lastName);
}