package org.example.repository;

import org.example.domain.user.Mentor;

import java.util.Optional;
import java.util.UUID;

public interface MentorRepository {
    Optional<Mentor> findTopBySkill(String skill);
    Optional<Mentor> findById(UUID id);
    boolean save(Mentor u);
}
