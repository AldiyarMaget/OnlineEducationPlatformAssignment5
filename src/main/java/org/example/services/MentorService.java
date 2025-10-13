package org.example.services;


import org.example.user.Student;
import org.example.components.Course;


import java.util.HashMap;
import java.util.Map;


public class MentorService {
    private final Map<String, String> mentorMap = new HashMap<>(); // enrollmentId -> mentorName (simple)


    public void assignMentor(Student student, Course course) {
        String key = student.getId() + "-" + course.getId();
        mentorMap.put(key, "Mentor-A");
        System.out.println("[MentorService] Mentor-A assigned to " + student.getName() + " for " + course.getName());
    }


    public void notifyMentorForFeedback(Student student, Course course) {
        String key = student.getId() + "-" + course.getId();
        String mentor = mentorMap.getOrDefault(key, "(no mentor)");
        System.out.println("[MentorService] Notifying " + mentor + " to provide feedback for " + student.getName() + " on " + course.getName());
    }
}