package org.example.app;

import org.example.components.Course;
import org.example.factory.CourseFactory;
import org.example.user.Student;

public class DemoRunner {
    private final StudentPortalFacade portal;

    public DemoRunner(StudentPortalFacade portal) {
        this.portal = portal;
    }

    public void run() {
        Student alice = new Student("s1", "Alice", "alice@example.com");
        Student bob   = new Student("s2", "Bob",   "bob@example.com");

        Course mathDecorated = CourseFactory.createCourse("math", true, true, false); // Mentor + Certificate
        Course progDecorated = CourseFactory.createCourse("programming", false, false, true); // Gamification

        portal.enrollInCourse(alice, mathDecorated);
        portal.enrollInCourse(bob, progDecorated);

        runAliceJourney(alice, mathDecorated);
        runBobJourney(bob, progDecorated);

        portal.printEnrollments(alice);
        portal.printEnrollments(bob);
    }

    private void runAliceJourney(Student s, Course c) {
        portal.startLearning(s, c, 40);
        portal.startLearning(s, c, 60);
    }

    private void runBobJourney(Student s, Course c) {
        portal.startLearning(s, c, 30);
        portal.startLearning(s, c, 30);
        portal.startLearning(s, c, 40);
    }
}
