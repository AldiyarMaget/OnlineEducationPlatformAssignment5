package org.example.components;


public class ProgrammingCourse implements Course {
    private final String id = "prog-201";
    private final String name = "Programming Course";


    @Override
    public String getId() { return id; }


    @Override
    public String getName() { return name; }


    @Override
    public String deliverContent() { return name; }
}