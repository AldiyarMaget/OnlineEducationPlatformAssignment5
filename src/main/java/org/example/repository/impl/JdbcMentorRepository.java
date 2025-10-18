package org.example.repository.impl;

import org.example.domain.user.Mentor;
import org.example.repository.MentorRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class JdbcMentorRepository implements MentorRepository {
    private final DataSource ds;

    public JdbcMentorRepository(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Optional<Mentor> findById(UUID id) {
        String sql = "SELECT id, name, email, skills, rating FROM mentors WHERE id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Mentor mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        Array arr = rs.getArray("skills");
        List<String> skills = new ArrayList<>();
        if (arr != null) {
            String[] arrVals = (String[]) arr.getArray();
            skills = Arrays.asList(arrVals);
        }
        double rating = rs.getDouble("rating");
        return new Mentor(id, name, email, skills, rating);
    }

    @Override
    public boolean save(Mentor user) {
        Mentor m = (Mentor) user;
        String sql = "INSERT INTO mentors(id, name, email, skills, rating) VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, email = EXCLUDED.email, skills = EXCLUDED.skills, rating = EXCLUDED.rating";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, m.getId());
            ps.setString(2, m.getName());
            ps.setString(3, m.getEmail());
            Connection conn = ps.getConnection();
            Array arr = conn.createArrayOf("text", m.getSkills().toArray(new String[0]));
            ps.setArray(4, arr);
            ps.setDouble(5, m.getRating());
            return ps.executeUpdate() >= 1;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    @Override
    public Optional<Mentor> findTopBySkill(String skill) {
        String sql = "SELECT id, name, email, skills, rating FROM mentors WHERE ? = ANY(skills) ORDER BY rating DESC LIMIT 1";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, skill);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
