package org.example.service;

import org.example.domain.enrollment.Certificate;

import java.util.UUID;

public interface CertificateService {
    Certificate issueCertificate(UUID studentId, String courseId);
}
