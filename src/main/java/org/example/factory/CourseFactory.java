package org.example.factory;

import org.example.domain.course.Course;

public interface CourseFactory {
    Course createCourse(String courseId);
    Course createCourseForEnrollment(String courseId, boolean useMentor, boolean useGamification, boolean useCertificate);
}
