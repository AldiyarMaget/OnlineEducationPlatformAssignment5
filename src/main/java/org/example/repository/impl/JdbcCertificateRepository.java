package org.example.repository.impl;

import org.example.domain.enrollment.Certificate;
import org.example.repository.CertificateRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public class JdbcCertificateRepository implements CertificateRepository {
    private final DataSource ds;
    public JdbcCertificateRepository(DataSource ds){ this.ds = ds; }

    @Override
    public Certificate save(Certificate cObj) {
        String sql = "INSERT INTO certificates(id, student_id, course_id, issued_at, url) VALUES (?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET url = EXCLUDED.url, issued_at = EXCLUDED.issued_at";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, cObj.getId());
            ps.setObject(2, cObj.getStudentId());
            ps.setString(3, cObj.getCourseId());
            ps.setObject(4, cObj.getIssuedAt());
            ps.setString(5, cObj.getUrl());
            ps.executeUpdate();
            return cObj;
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    @Override
    public Optional<Certificate> findByStudentAndCourse(UUID studentId, String courseId) {
        String sql = "SELECT id, student_id, course_id, issued_at, url FROM certificates WHERE student_id = ? AND course_id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, studentId);
            ps.setString(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Certificate(
                        (UUID) rs.getObject("id"),
                        (UUID) rs.getObject("student_id"),
                        rs.getString("course_id"),
                        rs.getObject("issued_at", OffsetDateTime.class),
                        rs.getString("url")
                ));
            }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }
}
