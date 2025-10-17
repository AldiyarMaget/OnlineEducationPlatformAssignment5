package org.example.repository;

import org.example.domain.user.Mentor;
import java.util.Optional;
import java.util.UUID;

public interface MentorRepository {
    Optional<Mentor> findById(UUID id);
    Optional<Mentor> top(UUID id);
    boolean save(Mentor m);
}
