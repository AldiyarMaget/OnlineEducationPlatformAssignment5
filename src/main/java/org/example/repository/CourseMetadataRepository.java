package org.example.repository;

import org.example.domain.course.CourseMetadata;
import java.util.Optional;

public interface CourseMetadataRepository {
    Optional<CourseMetadata> findMetadata(String courseId);
}
