package org.example.components;


public class MathCourse implements Course {
    private final String id = "math-101";
    private final String name = "Math Course";


    @Override
    public String getId() { return id; }


    @Override
    public String getName() { return name; }


    @Override
    public String deliverContent() { return name; }
}