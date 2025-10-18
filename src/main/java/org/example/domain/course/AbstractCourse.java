package org.example.domain.course;

import org.example.domain.user.Student;

public abstract class AbstractCourse implements Course {
    private final String id;
    private final String title;
    private final int totalModules;

    protected AbstractCourse(String id, String title, int totalModules) {
        this.id = id;
        this.title = title;
        this.totalModules = totalModules;
    }

    @Override
    public final String getId() { return id; }

    @Override
    public final String getTitle() { return title; }

    @Override
    public final int getTotalModules() { return totalModules; }

    @Override
    public void onEnroll(Student student) {
    }

    @Override
    public void onStart(Student student) {
    }

    @Override
    public void deliverContent(Student student) {
    }

    @Override
    public void onComplete(Student student) {
    }
}
