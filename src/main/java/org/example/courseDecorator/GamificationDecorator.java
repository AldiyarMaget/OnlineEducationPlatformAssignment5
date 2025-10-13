package org.example.courseDecorator;


import org.example.components.Course;
import org.example.contracts.Gamified;
import org.example.user.Student;


public class GamificationDecorator extends CourseDecorator implements Gamified {
    public GamificationDecorator(Course course) { super(course); }


    @Override
    public String deliverContent() {
        return super.deliverContent() + " + Gamification";
    }


    @Override
    public void onProgress(Student student, org.example.components.Course course, int delta) {
        System.out.println("[GamificationDecorator] Local: onProgress for " + student.getName() + " delta=" + delta);
    }


    @Override
    public void onCompletion(Student student, org.example.components.Course course) {
        System.out.println("[GamificationDecorator] Local: onCompletion for " + student.getName());
    }
}