package org.example.services;


import org.example.user.Student;


public class NotificationService {
    public void notifyStudent(Student s, String message) {
        System.out.println("[Notification] To " + s + " : " + message);
    }
}