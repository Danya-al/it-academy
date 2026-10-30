package ru.academy.textapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.academy.textapi.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
