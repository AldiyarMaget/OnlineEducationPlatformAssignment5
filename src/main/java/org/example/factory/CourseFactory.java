package org.example.factory;

import org.example.components.Course;
import org.example.components.MathCourse;
import org.example.components.ProgrammingCourse;
import org.example.courseDecorator.CertificateDecorator;
import org.example.courseDecorator.GamificationDecorator;
import org.example.courseDecorator.MentorSupportDecorator;

public final class CourseFactory {
    private CourseFactory() {}

    public static Course createCourse(String type, boolean withCertificate, boolean withMentor, boolean withGamification) {
        Course base;
        if (type == null) type = "";
        switch (type.toLowerCase()) {
            case "math":
            case "mathcourse":
            case "math-101":
                base = new MathCourse();
                break;
            case "programming":
            case "programmingcourse":
            case "prog-201":
                base = new ProgrammingCourse();
                break;
            default:
                throw new IllegalArgumentException("Unknown course type: " + type);
        }

        Course decorated = base;
        if (withMentor) decorated = new MentorSupportDecorator(decorated);
        if (withGamification) decorated = new GamificationDecorator(decorated);
        if (withCertificate) decorated = new CertificateDecorator(decorated);

        return decorated;
    }
}
