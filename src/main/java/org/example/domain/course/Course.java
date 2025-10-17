package org.example.domain.course;

import org.example.domain.user.Student;

public interface Course {
    String getId();
    String getTitle();
    int getTotalModules(); // количество модулей/уроков
    void onEnroll(Student student);
    void onStart(Student student);
    void deliverContent(Student student); // один модуль
    void onComplete(Student student);
}

