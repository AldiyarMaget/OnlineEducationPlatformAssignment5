package org.example.domain.course;

import org.example.domain.user.Student;
import org.example.factory.CourseRegistry;

public class MathCourse extends AbstractCourse {
    public MathCourse(String id, String title, int totalModules) {
        super(id, title, totalModules);
    }

    static {CourseRegistry.register("MATH", MathCourse::new); }


    @Override
    public void deliverContent(Student student) {
        super.deliverContent(student);
    }
}
