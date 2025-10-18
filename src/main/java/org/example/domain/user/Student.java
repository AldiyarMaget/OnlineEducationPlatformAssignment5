package org.example.domain.user;

import java.util.UUID;

public class Student extends User {
    public Student(UUID id, String name, String email) {
        super(id, name, email);
    }
}
