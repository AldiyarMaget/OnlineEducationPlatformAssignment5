package org.example.domain.enrollment;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Certificate {
    private final UUID id;
    private final UUID studentId;
    private final String courseId;
    private final OffsetDateTime issuedAt;
    private final String url;

    public Certificate(UUID id, UUID studentId, String courseId, OffsetDateTime issuedAt, String url) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.issuedAt = issuedAt;
        this.url = url;
    }

    public UUID getId() { return id; }
    public UUID getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public OffsetDateTime getIssuedAt() { return issuedAt; }
    public String getUrl() { return url; }
}
