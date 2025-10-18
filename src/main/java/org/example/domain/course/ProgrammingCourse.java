package org.example.domain.course;

import org.example.domain.user.Student;
import org.example.factory.CourseRegistry;

public class ProgrammingCourse extends AbstractCourse {
    public ProgrammingCourse(String id, String title, int totalModules) {
        super(id, title, totalModules);
    }

    static { org.example.factory.CourseRegistry.register("PROGRAMMING", ProgrammingCourse::new); }

    @Override
    public void deliverContent(Student student) {
        super.deliverContent(student);
    }
}
