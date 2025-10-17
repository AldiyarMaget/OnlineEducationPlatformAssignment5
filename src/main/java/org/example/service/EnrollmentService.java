package org.example.service;

public interface EnrollmentService {
    boolean enroll(String studentId, String courseId, boolean useMentor, boolean useGamification, boolean useCertificate);

    boolean start(String studentId, String courseId);

    int deliverModule(String studentId, String courseId);
    boolean complete(String studentId, String courseId);
}

