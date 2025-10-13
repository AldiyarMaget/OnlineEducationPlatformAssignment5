package org.example.app;

import org.example.components.Course;
import org.example.contracts.Certifiable;
import org.example.contracts.Gamified;
import org.example.contracts.HasMentor;
import org.example.services.CertificateService;
import org.example.services.GamificationService;
import org.example.services.MentorService;
import org.example.services.NotificationService;
import org.example.user.Student;

import java.util.HashMap;
import java.util.Map;

public class StudentPortalFacade {
    // studentId -> (courseId -> EnrolledCourse)
    private final Map<String, Map<String, EnrolledCourse>> enrollments = new HashMap<>();
    private final NotificationService notificationService = new NotificationService();
    private final GamificationService gamificationService = new GamificationService();
    private final CertificateService certificateService = new CertificateService();
    private final MentorService mentorService = new MentorService();

    public void enrollInCourse(Student student, Course course) {
        boolean hasCert = course instanceof Certifiable;
        boolean hasMentor = course instanceof HasMentor;
        boolean hasGamification = course instanceof Gamified;

        EnrolledCourse ec = new EnrolledCourse(course, hasCert, hasMentor, hasGamification);
        enrollments.computeIfAbsent(student.getId(), k -> new HashMap<>())
                .putIfAbsent(course.getId(), ec);

        notificationService.notifyStudent(student, "You have been enrolled in: " + course.deliverContent());

        if (hasMentor) {
            mentorService.assignMentor(student, course);
            notificationService.notifyStudent(student, "Mentor assigned to your course.");
            ((HasMentor) course).assignMentor(student, course);
        }
    }

    // overload: default delta
    public void startLearning(Student student, Course course) {
        startLearning(student, course, 20); // default delta = 20%
    }

    // main method that does the work (delta = how many percent to add)
    public void startLearning(Student student, Course course, int delta) {
        EnrolledCourse ec = findEnrolledCourse(student, course.getId());
        if (ec == null) {
            notificationService.notifyStudent(student, "You are not enrolled in this course.");
            return;
        }
        if (ec.isCompleted()) {
            notificationService.notifyStudent(student, "Course already completed.");
            return;
        }

        // update progress
        ec.addProgress(delta);
        // use deliverContent() to show full course name + features consistently
        notificationService.notifyStudent(student, "Progress for " + course.deliverContent() + " is now " + ec.getProgress() + "%.");

        // gamification: award points and call local decorator if present
        if (ec.hasGamification()) {
            int pts = Math.max(1, delta / 10); // simple rule: each 10% -> 1 point
            gamificationService.awardPoints(student, pts);
            if (course instanceof Gamified) ((Gamified) course).onProgress(student, course, delta);
        }

        // if completed after this session — finalize
        if (ec.isCompleted()) {
            completeCourse(student, course);
        }
    }

    public void completeCourse(Student student, Course course) {
        EnrolledCourse ec = findEnrolledCourse(student, course.getId());
        if (ec == null) {
            notificationService.notifyStudent(student, "You are not enrolled in this course.");
            return;
        }

        // 1) Mark enrollment as completed (if not already)
        if (!ec.isCompleted()) ec.complete();

        // 2) Notify student about completion (short, consistent name)
        notificationService.notifyStudent(student, "Congratulations! You completed " + course.deliverContent());

        // 3) Certificate: central service is the single source of truth now
        if (ec.hasCertificate()) {
            certificateService.createCertificate(student, course);
            // DON'T call the decorator's issueCertificate here to avoid duplication.
            notificationService.notifyStudent(student, "Certificate granted for: " + course.deliverContent());
        }

        // 4) Gamification: finishing bonus and decorator hook (decorator can still react locally if needed)
        if (ec.hasGamification()) {
            gamificationService.awardPoints(student, 10); // finishing bonus
            if (course instanceof Gamified) ((Gamified) course).onCompletion(student, course);
            notificationService.notifyStudent(student, "You earned a finishing bonus for completing a gamified course!");
        }

        // 5) Mentor: notify mentor and inform student that feedback will come
        if (ec.hasMentor()) {
            mentorService.notifyMentorForFeedback(student, course);
            // If decorator implements HasMentor, we can call its final feedback hook (optional)
            if (course instanceof HasMentor) ((HasMentor) course).requestFinalFeedback(student, course);
            notificationService.notifyStudent(student, "Your mentor will provide final feedback soon.");
        }
    }


    private EnrolledCourse findEnrolledCourse(Student student, String courseId) {
        Map<String, EnrolledCourse> map = enrollments.get(student.getId());
        if (map == null) return null;
        return map.get(courseId);
    }

    public void printEnrollments(Student student) {
        System.out.println("== Enrollments for " + student.getName() + " ==");
        Map<String, EnrolledCourse> map = enrollments.get(student.getId());
        if (map == null || map.isEmpty()) {
            System.out.println("No courses");
            return;
        }
        for (EnrolledCourse ec : map.values()) {
            System.out.println(ec.getCourse().deliverContent() + " - " + ec.getProgress() + "% - completed: " + ec.isCompleted());
        }
        System.out.println("Points: " + gamificationService.getPoints(student));
    }
}
