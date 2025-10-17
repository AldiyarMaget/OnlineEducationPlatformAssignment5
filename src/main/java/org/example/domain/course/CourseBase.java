package org.example.domain.course;

public class CourseBase {
    private final String id;
    private final String title;
    private final String type;
    private final int totalModules;

    public CourseBase(String id, String title, String type, int totalModules) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.totalModules = totalModules;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public int getTotalModules() { return totalModules; }
}
