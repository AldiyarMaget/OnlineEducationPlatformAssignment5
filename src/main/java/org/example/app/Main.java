package org.example.app;

public class Main {
    public static void main(String[] args) {
        StudentPortalFacade portal = new StudentPortalFacade();
        new DemoRunner(portal).run();
    }
}
