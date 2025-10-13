package org.example.services;


import org.example.user.Student;
import org.example.components.Course;


public class CertificateService {
    public void createCertificate(Student student, Course course) {
// in real app: generate PDF, store in DB; here: simple output
        System.out.println("[CertificateService] Certificate created for " + student.getName() + " - " + course.getName());
    }
}