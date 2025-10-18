package org.example.repository.impl;

import org.example.domain.enrollment.Enrollment;
import org.example.repository.EnrollmentRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class JdbcEnrollmentRepository implements EnrollmentRepository {
    private final DataSource ds;
    public JdbcEnrollmentRepository(DataSource ds){ this.ds = ds; }

    @Override
    public boolean create(Enrollment e) {
        String sql = "INSERT INTO enrollments(student_id, course_id, status, completed_modules, mentor_id, certificate_id, version, use_mentor, use_gamification, use_certificate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, e.getStudentId());
            ps.setString(2, e.getCourseId());
            ps.setString(3, e.getStatus());
            ps.setInt(4, e.getCompletedModules());
            ps.setObject(5, e.getMentorId());
            ps.setObject(6, e.getCertificateId());
            ps.setInt(7, e.getVersion());
            ps.setBoolean(8, e.isUseMentor());
            ps.setBoolean(9, e.isUseGamification());
            ps.setBoolean(10, e.isUseCertificate());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    @Override
    public Optional<Enrollment> findByStudentAndCourse(UUID studentId, String courseId) {
        String sql = "SELECT student_id, course_id, status, completed_modules, mentor_id, certificate_id, version, use_mentor, use_gamification, use_certificate FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, studentId); ps.setString(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    private Enrollment mapRow(ResultSet rs) throws SQLException {
        UUID sid = (UUID) rs.getObject("student_id");
        String cid = rs.getString("course_id");
        String status = rs.getString("status");
        int completed = rs.getInt("completed_modules");
        UUID mentorId = (UUID) rs.getObject("mentor_id");
        UUID certId = (UUID) rs.getObject("certificate_id");
        int version = rs.getInt("version");
        boolean useMentor = rs.getBoolean("use_mentor");
        boolean useGamification = rs.getBoolean("use_gamification");
        boolean useCertificate = rs.getBoolean("use_certificate");
        return new Enrollment(sid, cid, status, completed, mentorId, certId, version, useMentor, useGamification, useCertificate);
    }

    @Override
    public int incrementCompletedModules(UUID studentId, String courseId) {
        String sql = "UPDATE enrollments SET completed_modules = completed_modules + 1, version = version + 1 WHERE student_id = ? AND course_id = ? RETURNING completed_modules";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, studentId); ps.setString(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("completed_modules");
                throw new IllegalStateException("Enrollment not found");
            }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    @Override
    public boolean updateStatusIfVersionMatches(UUID studentId, String courseId, String newStatus, int expectedVersion) {
        String sql = "UPDATE enrollments SET status = ?, version = version + 1 WHERE student_id = ? AND course_id = ? AND version = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newStatus); ps.setObject(2, studentId); ps.setString(3, courseId); ps.setInt(4, expectedVersion);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    @Override
    public boolean setMentorId(UUID studentId, String courseId, UUID mentorId) {
        String sql = "UPDATE enrollments SET mentor_id = ?, use_mentor = TRUE WHERE student_id = ? AND course_id = ?";
        return updateEnrollmentSingleUuid(studentId, courseId, mentorId, sql);
    }

    private boolean updateEnrollmentSingleUuid(UUID studentId, String courseId, UUID mentorId, String sql) {
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, mentorId); ps.setObject(2, studentId); ps.setString(3, courseId);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    @Override
    public boolean setCertificateId(UUID studentId, String courseId, UUID certificateId) {
        String sql = "UPDATE enrollments SET certificate_id = ? WHERE student_id = ? AND course_id = ?";
        return updateEnrollmentSingleUuid(studentId, courseId, certificateId, sql);
    }
}
