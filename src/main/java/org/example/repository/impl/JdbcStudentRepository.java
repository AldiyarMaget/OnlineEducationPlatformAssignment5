package org.example.repository.impl;

import org.example.domain.user.Student;
import org.example.repository.StudentRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class JdbcStudentRepository implements StudentRepository {
    private final DataSource ds;
    public JdbcStudentRepository(DataSource ds){ this.ds = ds; }

    @Override
    public Optional<Student> findById(UUID studentId) {
        String sql = "SELECT id, name, email FROM students WHERE id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Student(
                        (UUID) rs.getObject("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    @Override
    public boolean save(Student u) {
        String sql = "INSERT INTO students(id, name, email) VALUES (?, ?, ?) ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, email = EXCLUDED.email";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, u.getId());
            ps.setString(2, u.getName());
            ps.setString(3, u.getEmail());
            return ps.executeUpdate() >= 1;
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }
}
