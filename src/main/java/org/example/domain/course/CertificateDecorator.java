package org.example.domain.course;

import org.example.domain.user.Student;
import org.example.service.CertificateService;

public class CertificateDecorator extends CourseDecorator {
    private final CertificateService cs;
    public CertificateDecorator(Course wrapped, CertificateService cs) { super(wrapped); this.cs = cs; }
    @Override
    public void onComplete(Student student) {
        super.onComplete(student);
        cs.issueCertificate(student.getId(), getId());
    }
}