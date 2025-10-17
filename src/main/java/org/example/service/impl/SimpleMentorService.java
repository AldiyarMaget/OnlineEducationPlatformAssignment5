package org.example.service.impl;

import org.example.domain.user.Mentor;
import org.example.repository.EnrollmentRepository;
import org.example.repository.MentorRepository;
import org.example.service.MentorService;

import java.util.Optional;
import java.util.UUID;

public class SimpleMentorService implements MentorService {
    private final MentorRepository mentorRepo;
    private final EnrollmentRepository enrollmentRepo;

    public SimpleMentorService(MentorRepository mentorRepo, EnrollmentRepository enrollmentRepo) {
        this.mentorRepo = mentorRepo;
        this.enrollmentRepo = enrollmentRepo;
    }

    @Override
    public UUID assignMentor(UUID studentId, String courseId) {
        String skill = courseIdToSkill(courseId);

        Optional<Mentor> mOpt = mentorRepo.findById(skill);
        if (mOpt.isEmpty()) return null;

        UUID mentorId = mOpt.get().getId();
        boolean ok = enrollmentRepo.setMentorId(studentId, courseId, mentorId);
        return ok ? mentorId : null;
    }

    private String courseIdToSkill(String courseId) {
        if (courseId == null) return "general";
        String lc = courseId.toLowerCase();
        if (lc.contains("prog") || lc.contains("code")) return "programming";
        if (lc.contains("math") || lc.contains("algebra")) return "math";
        return "general";
    }
}
