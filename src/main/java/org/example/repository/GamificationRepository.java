package org.example.repository;

import org.example.domain.enrollment.LeaderboardEntry;
import java.util.List;

import java.util.UUID;

public interface GamificationRepository {
    void addPoints(UUID studentId, String courseId, int points);
    long getPoints(UUID studentId, String courseId);
    List<LeaderboardEntry> getTop(int limit);
}
