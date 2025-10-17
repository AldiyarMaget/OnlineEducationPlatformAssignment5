package org.example.service.impl;

import org.example.domain.enrollment.LeaderboardEntry;
import org.example.repository.GamificationRepository;
import org.example.service.GamificationService;

import java.util.List;
import java.util.UUID;

public class SimpleGamificationService implements GamificationService {
    private final GamificationRepository gamRepo;

    public SimpleGamificationService(GamificationRepository gamRepo) {
        this.gamRepo = gamRepo;
    }

    @Override
    public void addPoints(UUID studentId, String courseId, int points) {
        gamRepo.addPoints(studentId, courseId, points);
    }

    @Override
    public long getPoints(UUID studentId, String courseId) {
        return gamRepo.getPoints(studentId, courseId);
    }

    @Override
    public void awardCompletionBadge(UUID studentId, String courseId) {
    }

    @Override
    public List<LeaderboardEntry> getLeaderboard(int limit) {
        return gamRepo.getTop(limit);
    }
}
