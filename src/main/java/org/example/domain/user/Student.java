package org.example.domain.user;

import java.util.UUID;

public abstract class Student implements User {
    private final UUID id;
    private final String name;
    private final String email;

    public Student(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}
