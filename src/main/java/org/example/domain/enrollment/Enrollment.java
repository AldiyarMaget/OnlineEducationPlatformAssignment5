package org.example.domain.enrollment;

import java.util.UUID;

public class Enrollment {
    private final UUID studentId;
    private final String courseId;
    private final String status;
    private final int completedModules;
    private final UUID mentorId;
    private final UUID certificateId;
    private final int version;
    private final boolean useMentor;
    private final boolean useGamification;
    private final boolean useCertificate;

    public Enrollment(UUID studentId, String courseId, String status, int completedModules,
                      UUID mentorId, UUID certificateId, int version,
                      boolean useMentor, boolean useGamification, boolean useCertificate) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = status;
        this.completedModules = completedModules;
        this.mentorId = mentorId;
        this.certificateId = certificateId;
        this.version = version;
        this.useMentor = useMentor;
        this.useGamification = useGamification;
        this.useCertificate = useCertificate;
    }

    public UUID getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public String getStatus() { return status; }
    public int getCompletedModules() { return completedModules; }
    public UUID getMentorId() { return mentorId; }
    public UUID getCertificateId() { return certificateId; }
    public int getVersion() { return version; }
    public boolean isUseMentor() { return useMentor; }
    public boolean isUseGamification() { return useGamification; }
    public boolean isUseCertificate() { return useCertificate; }
}
