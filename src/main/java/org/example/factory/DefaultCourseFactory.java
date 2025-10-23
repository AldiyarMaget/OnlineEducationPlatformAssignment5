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

    private DefaultCourseFactory(FactoryBuilder builder) {
        this.courseRepo = builder.courseRepo;
        this.metaRepo = builder.metaRepo;
        this.mentorService = builder.mentorService;
        this.gamificationService = builder.gamificationService;
        this.certificateService = builder.certificateService;

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
    public static class FactoryBuilder {
        private CourseRepository courseRepo;
        private CourseMetadataRepository metaRepo;
        private MentorService mentorService;
        private GamificationService gamificationService;
        private CertificateService certificateService;

        public FactoryBuilder courseRepo(CourseRepository courseRepo) {
            this.courseRepo = courseRepo;
            return this;
        }

        public FactoryBuilder metaRepo(CourseMetadataRepository metaRepo) {
            this.metaRepo = metaRepo;
            return this;
        }

        public FactoryBuilder mentorService(MentorService mentorService) {
            this.mentorService = mentorService;
            return this;
        }
        public FactoryBuilder gamificationService(GamificationService gamificationService) {
            this.gamificationService = gamificationService;
            return this;
        }

        public FactoryBuilder certificateService(CertificateService certificateService) {
            this.certificateService = certificateService;
            return this;
        }
        public DefaultCourseFactory build() {
            return new DefaultCourseFactory(this);
        }


    }
}
