package org.example.factory;

import org.example.repository.CourseMetadataRepository;
import org.example.repository.CourseRepository;
import org.example.service.CertificateService;
import org.example.service.GamificationService;
import org.example.service.MentorService;

public class FactoryBuilder {
    DefaultCourseFactory courseFactory;

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
        return new DefaultCourseFactory(FactoryBuilder);
    }


}
