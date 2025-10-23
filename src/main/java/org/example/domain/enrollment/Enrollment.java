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

    private Enrollment(EnrollmentBuilder builder) {
        this.studentId = builder.studentId;
        this.courseId = builder.courseId;
        this.status = builder.status;
        this.completedModules = builder.completedModules;
        this.mentorId = builder.mentorId;
        this.certificateId = builder.certificateId;
        this.version = builder.version;
        this.useMentor = builder.useMentor;
        this.useGamification = builder.useGamification;
        this.useCertificate = builder.useCertificate;
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

    public static class EnrollmentBuilder{
        private UUID studentId;
        private String courseId;
        private String status;
        private int completedModules;
        private UUID mentorId;
        private UUID certificateId;
        private int version;
        private boolean useMentor;
        private boolean useGamification;
        private boolean useCertificate;

        public EnrollmentBuilder studentId(UUID studentId) {
            this.studentId = studentId;
            return this;
        }
        public EnrollmentBuilder courseId(String courseId) {
            this.courseId = courseId;
            return this;
        }
        public EnrollmentBuilder status(String status) {
            this.status = status;
            return this;
        }
        public EnrollmentBuilder completedModules(int completedModules) {
            this.completedModules = completedModules;
            return this;
        }
        public EnrollmentBuilder mentorId(UUID mentorId) {
            this.mentorId = mentorId;
            return this;
        }
        public EnrollmentBuilder certificateId(UUID certificateId) {
            this.certificateId = certificateId;
            return this;
        }
        public EnrollmentBuilder version(int version) {
            this.version = version;
            return this;
        }
        public EnrollmentBuilder useMentor(boolean useMentor) {
            this.useMentor = useMentor;
            return this;
        }
        public EnrollmentBuilder useGamification(boolean useGamification) {
            this.useGamification = useGamification;
            return this;
        }
        public EnrollmentBuilder useCertificate(boolean useCertificate) {
            this.useCertificate = useCertificate;
            return this;
        }
        public Enrollment build() {
            return new Enrollment(this);
        }

    }
}
