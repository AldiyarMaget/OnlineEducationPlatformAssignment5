package org.example.repository;

import org.example.domain.user.Student;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository {
    Optional<Student> findById(UUID id);
    boolean save(Student u);
}
