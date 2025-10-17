package org.example.service;

import java.util.UUID;

public interface MentorService {
    UUID assignMentor(UUID studentId, String courseId);
}
