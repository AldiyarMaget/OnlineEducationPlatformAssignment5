package org.example.domain.course;

public class CourseMetadata {
    private final String courseId;
    private final boolean hasMentor;
    private final boolean hasCertificate;
    private final boolean hasGamification;
    private final int gamificationPointsPerModule;

    public CourseMetadata(String courseId, boolean hasMentor, boolean hasCertificate, boolean hasGamification, int gamificationPointsPerModule) {
        this.courseId = courseId;
        this.hasMentor = hasMentor;
        this.hasCertificate = hasCertificate;
        this.hasGamification = hasGamification;
        this.gamificationPointsPerModule = gamificationPointsPerModule;
    }

    public String getCourseId() { return courseId; }
    public boolean hasMentor() { return hasMentor; }
    public boolean hasCertificate() { return hasCertificate; }
    public boolean hasGamification() { return hasGamification; }
    public int getGamificationPointsPerModule() { return gamificationPointsPerModule; }
}
