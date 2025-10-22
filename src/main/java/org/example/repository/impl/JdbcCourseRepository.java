package org.example.repository.impl;

import org.example.domain.course.AbstractCourse;
import org.example.factory.CourseRegistry;
import org.example.repository.CourseRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

public class JdbcCourseRepository implements CourseRepository {
    private final DataSource ds;
    public JdbcCourseRepository(DataSource ds){ this.ds = ds; }

    @Override
    public Optional<AbstractCourse> findBaseCourse(String courseId) {
        String sql = "SELECT id, title, type, total_modules FROM courses WHERE id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                String id = rs.getString("id");
                String title = rs.getString("title");
                String type = rs.getString("type");
                int totalModules = rs.getInt("total_modules");

                AbstractCourse course = CourseRegistry.create(type, id, title, totalModules);
                return Optional.of(course);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
