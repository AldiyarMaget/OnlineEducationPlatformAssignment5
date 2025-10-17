package org.example.facade;

import org.example.service.EnrollmentService;

public class StudentPortalFacade {
    private final EnrollmentService enrollmentService;

    public StudentPortalFacade(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    public boolean enrollInCourse(String studentId, String courseId, boolean useMentor, boolean useGamification, boolean useCertificate) {
        return enrollmentService.enroll(studentId, courseId, useMentor, useGamification, useCertificate);
    }

    public boolean startLearning(String studentId, String courseId) {
        return enrollmentService.start(studentId, courseId);
    }

    public int deliverModule(String studentId, String courseId) {
        return enrollmentService.deliverModule(studentId, courseId);
    }

    public boolean completeCourse(String studentId, String courseId) {
        return enrollmentService.complete(studentId, courseId);
    }
}
