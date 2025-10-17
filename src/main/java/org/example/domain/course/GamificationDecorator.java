package org.example.domain.course;

import org.example.domain.user.Student;
import org.example.service.GamificationService;

import java.util.UUID;

public class GamificationDecorator extends CourseDecorator {
    private final GamificationService gamificationService;
    private final int pointsPerModule;

    public GamificationDecorator(Course wrapped, GamificationService gs, int pointsPerModule) {
        super(wrapped);
        this.gamificationService = gs;
        this.pointsPerModule = pointsPerModule;
    }

    @Override
    public void deliverContent(Student student) {
        super.deliverContent(student);
        UUID studentId = student.getId();
        gamificationService.addPoints(studentId, getId(), pointsPerModule);
    }

    @Override
    public void onComplete(Student student) {
        super.onComplete(student);
        gamificationService.awardCompletionBadge(student.getId(), getId());
    }
}
