package org.example.factory;

import org.example.domain.course.AbstractCourse;

@FunctionalInterface
public interface CourseCreator {
    AbstractCourse create(String id, String title, int totalModules);
}
