package org.example.contracts;


import org.example.user.Student;
import org.example.components.Course;


public interface HasMentor {
    void assignMentor(Student student, Course course);
    void requestFinalFeedback(Student student, Course course);
}