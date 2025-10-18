package org.example.domain.user;

import java.util.List;
import java.util.UUID;

public class Mentor extends User {
    private final List<String> skills;
    private final double rating;

    public Mentor(UUID id, String name, String email, List<String> skills, double rating) {
        super(id, name, email);
        this.skills = skills;
        this.rating = rating;
    }

    public List<String> getSkills() { return skills; }
    public double getRating() { return rating; }
}
