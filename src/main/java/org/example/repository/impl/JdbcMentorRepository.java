package org.example.repository.impl;

import org.example.domain.user.Mentor;
import org.example.repository.MentorRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class JdbcMentorRepository implements MentorRepository {
    private final DataSource ds;
    public JdbcMentorRepository(DataSource ds){ this.ds = ds; }

    @Override
    public Optional<Mentor> findById(UUID mentorId) {
        String sql = "SELECT id, name, email, skills, rating FROM mentors WHERE id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, mentorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                List<String> skills = arrayToList(rs.getArray("skills"));
                return Optional.of(new Mentor(
                        (UUID) rs.getObject("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        skills,
                        rs.getDouble("rating")));
            }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }



    @Override
    public boolean save(Mentor m) {
        String sql = "INSERT INTO mentors(id, name, email, skills, rating) VALUES (?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, email = EXCLUDED.email, skills = EXCLUDED.skills, rating = EXCLUDED.rating";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, m.getId());
            ps.setString(2, m.getName());
            ps.setString(3, m.getEmail());
            Array arr = c.createArrayOf("text", m.getSkills().toArray(new String[0]));
            ps.setArray(4, arr);
            ps.setDouble(5, m.getRating());
            return ps.executeUpdate() >= 1;
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    private static List<String> arrayToList(Array array) throws SQLException {
        if (array == null) return Collections.emptyList();
        Object[] objs = (Object[]) array.getArray();
        List<String> out = new ArrayList<>(objs.length);
        for (Object o : objs) out.add(o == null ? null : o.toString());
        return out;
    }
}
