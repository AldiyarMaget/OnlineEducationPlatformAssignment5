package org.example.repository.impl;

import org.example.domain.course.CourseBase;
import org.example.repository.CourseRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

public class JdbcCourseRepository implements CourseRepository {
    private final DataSource ds;
    public JdbcCourseRepository(DataSource ds){ this.ds = ds; }

    @Override
    public Optional<CourseBase> findBaseCourse(String courseId) {
        String sql = "SELECT id, title, type, total_modules FROM courses WHERE id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new CourseBase(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("type"),
                        rs.getInt("total_modules")
                ));
            }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }
}
