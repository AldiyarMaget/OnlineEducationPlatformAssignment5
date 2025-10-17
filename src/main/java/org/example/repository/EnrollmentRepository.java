package org.example.repository;

import org.example.domain.enrollment.Enrollment;
import org.example.domain.enrollment.Certificate;

import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository {
    boolean create(Enrollment e);
    Optional<Enrollment> findByStudentAndCourse(UUID studentId, String courseId);
    int incrementCompletedModules(UUID studentId, String courseId);
    boolean setCertificateId(UUID studentId, String courseId, UUID certificateId);
    boolean updateStatusIfVersionMatches(UUID studentId, String courseId, String newStatus, int expectedVersion);
    boolean setMentorId(UUID studentId, String courseId, UUID mentorId);
    Certificate saveCertificate(Certificate c);
    Optional<Certificate> findCertificate(UUID studentId, String courseId);
}
