package org.example.service.impl;

import org.example.domain.course.AbstractCourse;
import org.example.domain.course.Course;
import org.example.domain.enrollment.Certificate;
import org.example.domain.enrollment.Enrollment;
import org.example.domain.user.Student;
import org.example.factory.CourseFactory;
import org.example.repository.*;
import org.example.service.*;

import java.util.Optional;
import java.util.UUID;

public class EnrollmentServiceImpl implements EnrollmentService {
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;
    private final CourseMetadataRepository metaRepo;
    private final CertificateRepository certRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final MentorService mentorService;
    private final GamificationService gamificationService;
    private final CertificateService certificateService;
    private final NotificationService notificationService;
    private final CourseFactory courseFactory;

    public EnrollmentServiceImpl(
            StudentRepository studentRepo,
            CourseRepository courseRepo,
            CourseMetadataRepository metaRepo,
            CertificateRepository certRepo,
            EnrollmentRepository enrollmentRepo,
            MentorService mentorService,
            GamificationService gamificationService,
            CertificateService certificateService,
            NotificationService notificationService,
            CourseFactory courseFactory) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.metaRepo = metaRepo;
        this.certRepo = certRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.mentorService = mentorService;
        this.gamificationService = gamificationService;
        this.certificateService = certificateService;
        this.notificationService = notificationService;
        this.courseFactory = courseFactory;
    }

    @Override
    public boolean enroll(String studentId, String courseId, boolean useMentor, boolean useGamification, boolean useCertificate) {
        UUID sId = UUID.fromString(studentId);
        if (studentRepo.findById(sId).isEmpty()) throw new RuntimeException("Student not found");
        Enrollment e = new Enrollment.EnrollmentBuilder()
                .studentId(sId)
                .courseId(courseId)
                .status("ENROLLED")
                .completedModules(0)
                .version(0)
                .useMentor(useMentor)
                .useGamification(useGamification)
                .useCertificate(useCertificate)
                .build();
        boolean created = enrollmentRepo.create(e);


        if (!created) return false;

        Student student = studentRepo.findById(sId).orElse(new Student(sId, "", ""));
        Course runtime = courseFactory.createCourseForEnrollment(courseId, useMentor, useGamification, useCertificate);
        try {
            runtime.onEnroll(student);
        } catch (Exception ex) {
            notificationService.notifyAdmin("runtime onEnroll failed: " + ex.getMessage());
        }

        notificationService.notifyStudent(studentId, "Enrolled in " + courseId);
        return true;
    }


    @Override
    public boolean start(String studentId, String courseId) {
        UUID sId = UUID.fromString(studentId);
        Optional<Enrollment> eOpt = enrollmentRepo.findByStudentAndCourse(sId, courseId);
        if (eOpt.isEmpty()) throw new RuntimeException("Enrollment not found");
        Enrollment e = eOpt.get();
        boolean updated = enrollmentRepo.updateStatusIfVersionMatches(sId, courseId, "IN_PROGRESS", e.getVersion());
        if (!updated) {
            notificationService.notifyAdmin("Optimistic lock failed when setting IN_PROGRESS for " + sId + "/" + courseId);
        }
        Student student = studentRepo.findById(sId).orElse(new Student(sId, "", ""));
        try {
            courseFactory.createCourse(courseId).onStart(student);
        } catch (Exception ex) {
            notificationService.notifyAdmin("onStart failed for " + studentId + " / " + courseId + " : " + ex.getMessage());
        }

        notificationService.notifyStudent(studentId, "Course started: " + courseId);
        return true;
    }

    @Override
    public int deliverModule(String studentId, String courseId) {
        UUID sId = UUID.fromString(studentId);

        int newCompleted = enrollmentRepo.incrementCompletedModules(sId, courseId);

        Enrollment current = enrollmentRepo.findByStudentAndCourse(sId, courseId).orElseThrow();

        org.example.domain.course.Course runtimeCourse =
                courseFactory.createCourseForEnrollment(courseId, current.isUseMentor(), current.isUseGamification(), current.isUseCertificate());

        org.example.domain.user.Student student = studentRepo.findById(sId).orElseThrow();
        try {
            runtimeCourse.deliverContent(student);
        } catch (Exception ex) {
            notificationService.notifyAdmin("runtimeCourse.deliverContent failed for " + sId + "/" + courseId + " : " + ex.getMessage());
        }

        Optional<AbstractCourse> courseBaseOpt = courseRepo.findBaseCourse(courseId);
        if (courseBaseOpt.isPresent() && newCompleted >= courseBaseOpt.get().getTotalModules()) {
            Enrollment latest = enrollmentRepo.findByStudentAndCourse(sId, courseId).orElseThrow();
            boolean statusUpdated = enrollmentRepo.updateStatusIfVersionMatches(sId, courseId, "COMPLETED", latest.getVersion());
            if (!statusUpdated) {
                notificationService.notifyAdmin("Optimistic lock failed when setting COMPLETED for " + sId + "/" + courseId);
            }

            Enrollment after = enrollmentRepo.findByStudentAndCourse(sId, courseId).orElseThrow();
            org.example.domain.course.Course completionCourse =
                    courseFactory.createCourseForEnrollment(courseId, after.isUseMentor(), after.isUseGamification(), after.isUseCertificate());

            try {
                completionCourse.onComplete(student);
            } catch (Exception ex) {
                notificationService.notifyAdmin("runtimeCourse.onComplete failed for " + sId + "/" + courseId + " : " + ex.getMessage());
            }
            try {
                Optional<org.example.domain.enrollment.Certificate> certOpt = certRepo.findCertificate(sId, courseId);
                if (certOpt.isPresent()) {
                    enrollmentRepo.setCertificateId(sId, courseId, certOpt.get().getId());
                    notificationService.notifyStudent(studentId, "Course completed. Certificate: " + certOpt.get().getUrl());
                } else {
                    notificationService.notifyStudent(studentId, "Course completed.");
                }
            } catch (Exception ex) {
                notificationService.notifyAdmin("Failed to persist certificate id after completion for " + sId + "/" + courseId + " : " + ex.getMessage());
            }
        } else {
            notificationService.notifyStudent(studentId, "Module completed: " + newCompleted);
        }

        return newCompleted;
    }


    @Override
    public boolean complete(String studentId, String courseId) {
        UUID sId = UUID.fromString(studentId);
        Enrollment e = enrollmentRepo.findByStudentAndCourse(sId, courseId).orElseThrow();
        boolean updated = enrollmentRepo.updateStatusIfVersionMatches(sId, courseId, "COMPLETED", e.getVersion());
        if (!updated) {
            notificationService.notifyAdmin("Optimistic lock failed when forcing COMPLETED for " + sId + "/" + courseId);
        }
        if (certRepo.findCertificate(sId, courseId).isEmpty()) {
            Certificate cert = certificateService.issueCertificate(sId, courseId);
            try {
                certRepo.save(cert);
            } catch (AbstractMethodError | NoSuchMethodError ignored) { }
            notificationService.notifyStudent(studentId, "Certificate issued: " + cert.getUrl());
        }
        return true;
    }
}
