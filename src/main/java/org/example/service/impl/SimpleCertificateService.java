package org.example.service.impl;

import org.example.domain.enrollment.Certificate;
import org.example.repository.CertificateRepository;
import org.example.service.CertificateService;

import java.time.OffsetDateTime;
import java.util.UUID;

public class SimpleCertificateService implements CertificateService {
    private final CertificateRepository certRepo;

    public SimpleCertificateService(CertificateRepository certRepo) {
        this.certRepo = certRepo;
    }

    @Override
    public Certificate issueCertificate(UUID studentId, String courseId) {
        UUID certId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        String url = "/certs/" + certId + ".pdf"; // пох

        Certificate cert = new Certificate(certId, studentId, courseId, now, url);
        certRepo.save(cert);
        return cert;
    }
}
