package ru.academy.textapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.academy.textapi.entity.Reader;

public interface ReaderRepository extends JpaRepository<Reader,Long> {
}
