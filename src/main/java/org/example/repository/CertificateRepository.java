package org.example.repository;

import org.example.domain.enrollment.Certificate;
import java.util.Optional;
import java.util.UUID;

public interface CertificateRepository {
    Certificate save(Certificate c);
    Optional<Certificate> findCertificate(UUID studentId, String courseId);
    Optional<Certificate> findByStudentAndCourse(UUID studentId, String courseId);
}
