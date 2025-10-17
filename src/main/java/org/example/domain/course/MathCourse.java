package org.example.domain.course;

import org.example.domain.user.Student;

public class MathCourse extends AbstractCourse {
    public MathCourse(String id, String title, int totalModules) {
        super(id, title, totalModules);
    }

    @Override
    public void deliverContent(Student student) {
        super.deliverContent(student);
    }
}
