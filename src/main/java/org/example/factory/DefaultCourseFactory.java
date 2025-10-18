package org.example.factory;

import org.example.domain.course.*;
import org.example.repository.CourseMetadataRepository;
import org.example.repository.CourseRepository;
import org.example.service.CertificateService;
import org.example.service.GamificationService;
import org.example.service.MentorService;

import java.util.Optional;

public class DefaultCourseFactory implements CourseFactory {
    private final CourseRepository courseRepo;
    private final CourseMetadataRepository metaRepo;
    private final MentorService mentorService;
    private final GamificationService gamificationService;
    private final CertificateService certificateService;

    public DefaultCourseFactory(CourseRepository courseRepo,
                                CourseMetadataRepository metaRepo,
                                MentorService mentorService,
                                GamificationService gamificationService,
                                CertificateService certificateService) {
        this.courseRepo = courseRepo;
        this.metaRepo = metaRepo;
        this.mentorService = mentorService;
        this.gamificationService = gamificationService;
        this.certificateService = certificateService;

        org.example.factory.CourseRegistry.register("MATH", org.example.domain.course.MathCourse::new);
        org.example.factory.CourseRegistry.register("PROGRAMMING", org.example.domain.course.ProgrammingCourse::new);
    }


    @Override
    public Course createCourse(String courseId) {
        Optional<AbstractCourse> baseOpt = courseRepo.findBaseCourse(courseId);
        return baseOpt.orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
    }

    @Override
    public Course createCourseForEnrollment(String courseId, boolean useMentor, boolean useGamification, boolean useCertificate) {
        Course base = createCourse(courseId);
        Optional<CourseMetadata> metaOpt = metaRepo.findMetadata(courseId);

        boolean metaMentor = metaOpt.map(CourseMetadata::hasMentor).orElse(false);
        boolean metaGam = metaOpt.map(CourseMetadata::hasGamification).orElse(false);
        boolean metaCert = metaOpt.map(CourseMetadata::hasCertificate).orElse(false);
        int pointsPerModule = metaOpt.map(CourseMetadata::getGamificationPointsPerModule).orElse(0);

        Course c = base;
        if (useMentor && metaMentor) {
            c = new MentorSupportDecorator(c, mentorService);
        }
        if (useCertificate && metaCert) {
            c = new CertificateDecorator(c, certificateService);
        }
        if (useGamification && metaGam) {
            c = new GamificationDecorator(c, gamificationService, pointsPerModule);
        }
        return c;
    }
}
