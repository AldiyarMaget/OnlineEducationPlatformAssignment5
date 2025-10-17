package org.example.domain.user;

import java.util.List;
import java.util.UUID;

public abstract class Mentor implements User {
    private final UUID id;
    private final String name;
    private final String email;
    private final List<String> skills;
    private final double rating;

    public Mentor(UUID id, String name, String email, List<String> skills, double rating) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.skills = skills;
        this.rating = rating;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getSkills() { return skills; }
    public double getRating() { return rating; }
}
