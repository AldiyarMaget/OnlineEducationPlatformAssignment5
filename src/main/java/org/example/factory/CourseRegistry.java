package org.example.factory;

import org.example.domain.course.AbstractCourse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Locale;

public final class CourseRegistry {
    private static final Map<String, CourseCreator> REG = new ConcurrentHashMap<>();
    private CourseRegistry(){}

    public static void register(String key, CourseCreator creator) {
        REG.put(key.toUpperCase(Locale.ROOT), creator);
    }

    public static AbstractCourse create(String key, String id, String title, int totalModules) {
        CourseCreator cr = REG.get(key.toUpperCase(Locale.ROOT));
        if (cr == null) throw new IllegalArgumentException("Unknown course type: " + key);
        return cr.create(id, title, totalModules);
    }
}
