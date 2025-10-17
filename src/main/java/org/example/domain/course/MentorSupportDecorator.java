package org.example.domain.course;

import org.example.domain.user.Student;
import org.example.service.MentorService;

public class MentorSupportDecorator extends CourseDecorator {
    private final MentorService mentorService;

    public MentorSupportDecorator(Course wrapped, MentorService ms) {
        super(wrapped);
        this.mentorService = ms;
    }

    @Override
    public void onEnroll(Student student) {
        super.onEnroll(student);
        mentorService.assignMentor(student.getId(), getId());
    }
}
