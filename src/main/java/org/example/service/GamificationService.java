package org.example.service;

import org.example.domain.enrollment.LeaderboardEntry;

import java.util.List;
import java.util.UUID;

public interface GamificationService {
    void addPoints(UUID studentId, String courseId, int points);
    long getPoints(UUID studentId, String courseId);
    void awardCompletionBadge(UUID studentId, String courseId);
    List<LeaderboardEntry> getLeaderboard(int limit);
}
