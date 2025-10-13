package org.example.services;


import org.example.user.Student;


import java.util.HashMap;
import java.util.Map;


public class GamificationService {
    private final Map<String, Integer> points = new HashMap<>(); // studentId -> points


    public void awardPoints(Student student, int pts) {
        if (pts <= 0) return;
        points.put(student.getId(), points.getOrDefault(student.getId(), 0) + pts);
        System.out.println("[Gamification] Awarded " + pts + " points to " + student.getName()
                + ". Total: " + points.get(student.getId()));
    }


    public int getPoints(Student student) {
        return points.getOrDefault(student.getId(), 0);
    }
}