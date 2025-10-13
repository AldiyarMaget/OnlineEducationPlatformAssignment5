package org.example.contracts;


import org.example.user.Student;
import org.example.components.Course;


public interface Certifiable {
    void issueCertificate(Student student, Course course);
}