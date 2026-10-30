package ru.academy.textapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.academy.textapi.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}