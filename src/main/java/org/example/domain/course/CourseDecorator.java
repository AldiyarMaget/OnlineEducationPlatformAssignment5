package org.example.domain.course;

import org.example.domain.user.Student;

public abstract class CourseDecorator implements Course {
    protected final Course wrapped;
    public CourseDecorator(Course wrapped) { this.wrapped = wrapped; }
    public String getId() { return wrapped.getId(); }
    public String getTitle() { return wrapped.getTitle(); }
    public int getTotalModules() { return wrapped.getTotalModules(); }
    public void onEnroll(Student s) { wrapped.onEnroll(s); }
    public void onStart(Student s) { wrapped.onStart(s); }
    public void deliverContent(Student s) { wrapped.deliverContent(s); }
    public void onComplete(Student s) { wrapped.onComplete(s); }
}
