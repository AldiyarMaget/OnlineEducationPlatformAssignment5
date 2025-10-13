package org.example.courseDecorator;


import org.example.components.Course;


public abstract class CourseDecorator implements Course {
    protected final Course wrapped;
    public CourseDecorator(Course wrapped) { this.wrapped = wrapped; }
    public Course getWrapped() { return wrapped; }


    @Override
    public String getId() { return wrapped.getId(); }


    @Override
    public String getName() { return wrapped.getName(); }


    @Override
    public String deliverContent() { return wrapped.deliverContent(); }
}