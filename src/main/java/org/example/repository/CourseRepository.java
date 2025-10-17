package org.example.repository;

import org.example.domain.course.CourseBase;
import java.util.Optional;

public interface CourseRepository {
    Optional<CourseBase> findBaseCourse(String courseId);
}
