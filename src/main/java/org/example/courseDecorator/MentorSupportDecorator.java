package org.example.courseDecorator;


import org.example.components.Course;
import org.example.contracts.HasMentor;
import org.example.user.Student;


public class MentorSupportDecorator extends CourseDecorator implements HasMentor {
    public MentorSupportDecorator(Course course) { super(course); }


    @Override
    public String deliverContent() {
        return super.deliverContent() + " + MentorSupport";
    }


    @Override
    public void assignMentor(Student student, org.example.components.Course course) {
        System.out.println("[MentorSupportDecorator] Local: mentor assigned to " + student.getName() + " for " + course.getName());
    }


    @Override
    public void requestFinalFeedback(Student student, org.example.components.Course course) {
        System.out.println("[MentorSupportDecorator] Local: mentor will provide final feedback to " + student.getName() + " for " + course.getName());
    }
}