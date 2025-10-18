package org.example.domain.course;

import org.example.domain.user.Student;

public interface Course {
    String getId();
    String getTitle();
    int getTotalModules();
    void onEnroll(Student student);
    void onStart(Student student);
    void deliverContent(Student student);
    void onComplete(Student student);
}

