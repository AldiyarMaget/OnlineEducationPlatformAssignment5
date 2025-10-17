package org.example.service;

public interface NotificationService {
    void notifyStudent(String studentId, String message);
    void notifyAdmin(String message);
}
