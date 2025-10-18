package org.example.repository;

import org.example.domain.course.AbstractCourse;
import java.util.Optional;

public interface CourseRepository {
    Optional<AbstractCourse> findBaseCourse(String courseId);
}
