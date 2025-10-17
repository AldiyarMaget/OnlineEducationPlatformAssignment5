package org.example.repository.impl;

import org.example.domain.course.CourseMetadata;
import org.example.repository.CourseMetadataRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

public class JdbcCourseMetadataRepository implements CourseMetadataRepository {
    private final DataSource ds;
    public JdbcCourseMetadataRepository(DataSource ds){ this.ds = ds; }

    @Override
    public Optional<CourseMetadata> findMetadata(String courseId) {
        String sql = "SELECT course_id, has_mentor, has_certificate, has_gamification, gamification_points_per_module FROM course_metadata WHERE course_id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new CourseMetadata(
                        rs.getString("course_id"),
                        rs.getBoolean("has_mentor"),
                        rs.getBoolean("has_certificate"),
                        rs.getBoolean("has_gamification"),
                        rs.getInt("gamification_points_per_module")
                ));
            }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }
}
