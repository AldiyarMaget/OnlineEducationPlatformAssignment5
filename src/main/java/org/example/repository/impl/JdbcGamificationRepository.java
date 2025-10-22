package org.example.repository.impl;

import org.example.domain.enrollment.LeaderboardEntry;
import org.example.repository.GamificationRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcGamificationRepository implements GamificationRepository {
    private final DataSource ds;
    public JdbcGamificationRepository(DataSource ds){
        this.ds = ds;
    }

    @Override
    public void addPoints(UUID studentId, String courseId, int points) {
        String sql = "INSERT INTO gamification_points(student_id, course_id, points) VALUES (?, ?, ?) " +
                "ON CONFLICT (student_id, course_id) DO UPDATE SET points = gamification_points.points + EXCLUDED.points";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, studentId);
            ps.setString(2, courseId);
            ps.setInt(3, points);
            ps.executeUpdate();
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    @Override
    public long getPoints(UUID studentId, String courseId) {
        String sql = "SELECT points FROM gamification_points WHERE student_id = ? AND course_id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, studentId);
            ps.setString(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return 0L;
                return rs.getLong("points");
            }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    @Override
    public List<LeaderboardEntry> getTop(int limit) {
        String sql = "SELECT student_id, SUM(points) as total FROM gamification_points GROUP BY student_id ORDER BY total DESC LIMIT ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<LeaderboardEntry> out = new ArrayList<>();
                while (rs.next()){
                    out.add(new LeaderboardEntry((UUID)rs.getObject("student_id"), rs.getLong("total")));
                }
                return out;
            }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }
}
