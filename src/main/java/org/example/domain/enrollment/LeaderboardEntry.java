package org.example.domain.enrollment;

import java.util.UUID;

public class LeaderboardEntry {
    private final UUID studentId;
    private final long points;

    public LeaderboardEntry(UUID studentId, long points) {
        this.studentId = studentId;
        this.points = points;
    }

    public UUID getStudentId() { return studentId; }
    public long getPoints() { return points; }
}
