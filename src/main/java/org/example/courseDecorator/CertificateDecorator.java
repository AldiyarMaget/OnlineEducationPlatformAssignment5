package org.example.courseDecorator;


import org.example.components.Course;
import org.example.contracts.Certifiable;
import org.example.user.Student;


public class CertificateDecorator extends CourseDecorator implements Certifiable {
    public CertificateDecorator(Course course) { super(course); }


    @Override
    public String deliverContent() {
        return super.deliverContent() + " + Certificate";
    }


    @Override
    public void issueCertificate(Student student, org.example.components.Course course) {
        // no-op: actual certificate creation is handled centrally by CertificateService.
        // If you want local behavior for debugging, add a debug-level log here instead.
        // System.out.println("[CertificateDecorator] (debug) issueCertificate called for " + student.getName());
    }

}