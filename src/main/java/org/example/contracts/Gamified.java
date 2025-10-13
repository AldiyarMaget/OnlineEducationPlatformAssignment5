package org.example.contracts;


import org.example.user.Student;
import org.example.components.Course;


public interface Gamified {
    void onProgress(Student student, Course course, int delta);
    void onCompletion(Student student, Course course);
}