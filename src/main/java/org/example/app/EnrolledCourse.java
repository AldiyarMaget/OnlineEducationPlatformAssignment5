package org.example.app;


import org.example.components.Course;


import java.util.UUID;


public class EnrolledCourse {
    private final String enrollmentId;
    private final String courseId;
    private final Course course;
    private int progress; // 0..100
    private boolean completed;
    private final boolean hasCertificate;
    private final boolean hasMentor;
    private final boolean hasGamification;


    public EnrolledCourse(Course course, boolean hasCertificate, boolean hasMentor, boolean hasGamification) {
        this.enrollmentId = UUID.randomUUID().toString();
        this.course = course;
        this.courseId = course.getId();
        this.progress = 0;
        this.completed = false;
        this.hasCertificate = hasCertificate;
        this.hasMentor = hasMentor;
        this.hasGamification = hasGamification;
    }


    public String getEnrollmentId() { return enrollmentId; }
    public String getCourseId() { return courseId; }
    public Course getCourse() { return course; }
    public int getProgress() { return progress; }
    public boolean isCompleted() { return completed; }
    public boolean hasCertificate() { return hasCertificate; }
    public boolean hasMentor() { return hasMentor; }
    public boolean hasGamification() { return hasGamification; }


    // Controlled mutation
    public void addProgress(int delta) {
        if (delta <= 0) return;
        this.progress = Math.min(100, this.progress + delta);
        if (this.progress >= 100) {
            this.progress = 100;
            this.completed = true;
        }
    }


    public void complete() {
        this.progress = 100;
        this.completed = true;
    }
}