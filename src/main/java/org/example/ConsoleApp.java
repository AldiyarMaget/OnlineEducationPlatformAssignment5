package org.example;

import org.postgresql.ds.PGSimpleDataSource;
import org.example.repository.impl.*;
import org.example.repository.*;
import org.example.service.*;
import org.example.service.impl.*;
import org.example.factory.DefaultCourseFactory;
import org.example.facade.StudentPortalFacade;
import org.example.domain.enrollment.Certificate;
import org.example.domain.enrollment.Enrollment;
import org.example.domain.course.CourseMetadata;
import org.example.domain.user.Student;
import org.example.domain.enrollment.LeaderboardEntry;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class ConsoleApp {
    private final DataSource ds;

    private final CourseRepository courseRepo;
    private final CourseMetadataRepository metaRepo;
    private final StudentRepository studentRepo;
    private final MentorRepository mentorRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final CertificateRepository certRepo;
    private final GamificationRepository gamiRepo;

    private final GamificationService gamService;
    private final CertificateService certificateService;
    private final NotificationService notificationService;
    private final EnrollmentService enrollmentService;
    private final StudentPortalFacade facade;

    private final Scanner scanner = new Scanner(System.in);

    public ConsoleApp(String address, String user, String password) {
        PGSimpleDataSource dsLocal = new PGSimpleDataSource();
        dsLocal.setUrl(address);
        dsLocal.setUser(user);
        dsLocal.setPassword(password);
        this.ds = dsLocal;

        this.courseRepo = new JdbcCourseRepository(ds);
        this.metaRepo = new JdbcCourseMetadataRepository(ds);
        this.studentRepo = new JdbcStudentRepository(ds);
        this.mentorRepo = new JdbcMentorRepository(ds);
        this.enrollmentRepo = new JdbcEnrollmentRepository(ds);
        this.certRepo = new JdbcCertificateRepository(ds);
        this.gamiRepo = new JdbcGamificationRepository(ds);

        MentorService localMentorService = new SimpleMentorService(mentorRepo, enrollmentRepo);

        this.gamService = new SimpleGamificationService(gamiRepo);
        this.certificateService = new SimpleCertificateService(certRepo);

        this.notificationService = new NotificationService() {
            @Override public void notifyStudent(String studentId, String message) {
                System.out.println("[notify] to " + studentId + ": " + message);
            }
            @Override public void notifyAdmin(String message) {
                System.err.println("[admin] " + message);
            }
        };

        DefaultCourseFactory localFactory = new DefaultCourseFactory(courseRepo, metaRepo, localMentorService, gamService, certificateService);

        this.enrollmentService = new EnrollmentServiceImpl(
                studentRepo, courseRepo, metaRepo,certRepo, enrollmentRepo,
                localMentorService, gamService, certificateService, notificationService, localFactory
        );

        this.facade = new StudentPortalFacade(enrollmentService);
    }

    public void run() {
        System.out.println("Welcome to Console LMS. Type number for action");
        while (true) {
            printMenu();
            String s = scanner.nextLine().trim();
            if (s.isEmpty()) continue;
            switch (s) {
                case "1": registerStudent(); break;
                case "2": listCourses(); break;
                case "3": enrollStudent(); break;
                case "4": startCourse(); break;
                case "5": deliverModule(); break;
                case "6": completeCourse(); break;
                case "7": viewEnrollment(); break;
                case "8": viewCertificates(); break;
                case "9": showLeaderboard(); break;
                case "0": System.out.println("Bye"); return;
                default: System.out.println("Unknown command"); break;
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Menu ===");
        System.out.println("1 - Register student");
        System.out.println("2 - List courses");
        System.out.println("3 - Enroll student");
        System.out.println("4 - Start course");
        System.out.println("5 - Deliver module (complete one module)");
        System.out.println("6 - Complete course");
        System.out.println("7 - View enrollment / progress");
        System.out.println("8 - View certificates");
        System.out.println("9 - Leaderboard");
        System.out.println("0 - Exit");
        System.out.print("> ");
    }

    private void registerStudent() {
        try {
            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();

            UUID id = UUID.randomUUID();
            Student s = new Student(id, name, email);
            boolean ok = studentRepo.save(s);
            if (ok) {
                System.out.println("Student created. id = " + id);
            } else {
                System.out.println("Failed to save student.");
            }
        } catch (Exception ex) {
            notificationService.notifyAdmin("registerStudent failed: " + ex.getMessage());
            System.out.println("Error while registering student. Contact admin.");
        }
    }

    private void listCourses() {
        System.out.println("Available courses:");
        String sql = "SELECT id, title, type, total_modules FROM courses ORDER BY id";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("id");
                String title = rs.getString("title");
                String type = rs.getString("type");
                int tm = rs.getInt("total_modules");
                System.out.printf(" - %s: %s (%s) modules=%d%n", id, title, type, tm);
            }
        } catch (SQLException e) {
            notificationService.notifyAdmin("listCourses failed: " + e.getMessage());
            System.out.println("Failed to list courses. Contact admin.");
        }
    }

    private void enrollStudent() {
        try {
            System.out.print("Enter your student id (or type 'list' to see students): ");
            String sidInput = scanner.nextLine().trim();
            if (sidInput.equalsIgnoreCase("list")) {
                listStudents();
                System.out.print("Enter your student id: ");
                sidInput = scanner.nextLine().trim();
            }
            if (sidInput.isEmpty()) { System.out.println("Cancelled."); return; }
            UUID studentUuid;
            try { studentUuid = UUID.fromString(sidInput); } catch (IllegalArgumentException ex) { System.out.println("Bad UUID."); return; }
            String studentId = studentUuid.toString();

            System.out.print("Enter course id (e.g. MATH101): ");
            String courseId = scanner.nextLine().trim();
            if (courseId.isEmpty()) { System.out.println("Cancelled."); return; }

            Optional<CourseMetadata> meta = metaRepo.findMetadata(courseId);

            System.out.print("Use mentor? (yes/no) [enter = default from metadata]: ");
            String mentorAns = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            boolean useMentor = mentorAns.isEmpty() ? meta.map(CourseMetadata::hasMentor).orElse(false) : mentorAns.startsWith("y");

            System.out.print("Use gamification? (yes/no) [enter = default from metadata]: ");
            String gamAns = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            boolean useGamification = gamAns.isEmpty() ? meta.map(CourseMetadata::hasGamification).orElse(false) : gamAns.startsWith("y");

            System.out.print("Use certificate? (yes/no) [enter = default from metadata]: ");
            String certAns = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            boolean useCertificate = certAns.isEmpty() ? meta.map(CourseMetadata::hasCertificate).orElse(false) : certAns.startsWith("y");

            boolean ok = facade.enrollInCourse(studentId, courseId, useMentor, useGamification, useCertificate);
            System.out.println("Enroll request submitted (ok=" + ok + ")");
        } catch (Exception ex) {
            notificationService.notifyAdmin("enrollStudent failed: " + ex.getMessage());
            System.out.println("Error while enrolling. Contact admin.");
        }
    }


    private void startCourse() {
        try {
            String studentId = promptStudentId();
            if (studentId == null) return;
            System.out.print("Enter course id: ");
            String courseId = scanner.nextLine().trim();
            facade.startLearning(studentId, courseId);
            System.out.println("Started (or attempted to start) course.");
        } catch (Exception ex) {
            notificationService.notifyAdmin("startCourse failed: " + ex.getMessage());
            System.out.println("Error while starting course. Contact admin.");
        }
    }

    private void deliverModule() {
        try {
            String studentId = promptStudentId();
            if (studentId == null) return;
            System.out.print("Enter course id: ");
            String courseId = scanner.nextLine().trim();
            int completed = facade.deliverModule(studentId, courseId);
            System.out.println("Module delivered. Completed modules: " + completed);
        } catch (Exception ex) {
            notificationService.notifyAdmin("deliverModule failed: " + ex.getMessage());
            System.out.println("Error while delivering module. Contact admin.");
        }
    }

    private void completeCourse() {
        try {
            String studentId = promptStudentId();
            if (studentId == null) return;
            System.out.print("Enter course id: ");
            String courseId = scanner.nextLine().trim();
            facade.completeCourse(studentId, courseId);
            System.out.println("Complete command executed.");
        } catch (Exception ex) {
            notificationService.notifyAdmin("completeCourse failed: " + ex.getMessage());
            System.out.println("Error while completing course. Contact admin.");
        }
    }

    private void viewEnrollment() {
        try {
            String studentId = promptStudentId();
            if (studentId == null) return;
            System.out.print("Enter course id: ");
            String courseId = scanner.nextLine().trim();
            UUID sUuid = UUID.fromString(studentId);
            Optional<Enrollment> eOpt = enrollmentRepo.findByStudentAndCourse(sUuid, courseId);
            if (eOpt.isEmpty()) {
                System.out.println("No enrollment found.");
                return;
            }
            Enrollment e = eOpt.get();
            System.out.println("Status: " + e.getStatus());
            System.out.println("Completed modules: " + e.getCompletedModules());
            System.out.println("Mentor id: " + e.getMentorId());
            System.out.println("Certificate id: " + e.getCertificateId());
            System.out.println("Version: " + e.getVersion());
        } catch (Exception ex) {
            notificationService.notifyAdmin("viewEnrollment failed: " + ex.getMessage());
            System.out.println("Error while reading enrollment. Contact admin.");
        }
    }

    private void viewCertificates() {
        try {
            String studentId = promptStudentId();
            if (studentId == null) return;
            UUID s = UUID.fromString(studentId);
            System.out.print("Enter course id to view certificate (or blank to list all for student): ");
            String courseId = scanner.nextLine().trim();
            if (courseId.isEmpty()) {
                String sql = "SELECT id, course_id, issued_at, url FROM certificates WHERE student_id = ?";
                try (Connection conn = ds.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setObject(1, s);
                    try (ResultSet rs = ps.executeQuery()) {
                        boolean any = false;
                        while (rs.next()) {
                            any = true;
                            UUID id = (UUID) rs.getObject("id");
                            String cId = rs.getString("course_id");
                            Timestamp ts = rs.getTimestamp("issued_at");
                            String url = rs.getString("url");
                            System.out.println(" - cert " + id + " course=" + cId + " issued_at=" + (ts==null?null:ts.toInstant()) + " url=" + url);
                        }
                        if (!any) System.out.println("No certificates found for student.");
                    }
                }
            } else {
                Optional<Certificate> cOpt = certRepo.findByStudentAndCourse(s, courseId);
                if (cOpt.isPresent()) {
                    Certificate cert = cOpt.get();
                    System.out.println("Certificate id: " + cert.getId());
                    System.out.println("Course: " + cert.getCourseId());
                    System.out.println("Issued at: " + cert.getIssuedAt());
                    System.out.println("URL: " + cert.getUrl());
                } else {
                    System.out.println("No certificate found for student/course.");
                }
            }
        } catch (Exception ex) {
            notificationService.notifyAdmin("viewCertificates failed: " + ex.getMessage());
            System.out.println("Error while reading certificates. Contact admin.");
        }
    }

    private void showLeaderboard() {
        try {
            System.out.print("Top N (enter number): ");
            String nStr = scanner.nextLine().trim();
            int n;
            try { n = Integer.parseInt(nStr); } catch (NumberFormatException ex) { n = 10; }
            List<LeaderboardEntry> top = gamService.getLeaderboard(n);
            System.out.println("=== Leaderboard ===");
            for (int i = 0; i < top.size(); i++) {
                LeaderboardEntry e = top.get(i);
                System.out.printf("%d) %s -> %d points%n", i+1, e.getStudentId(), e.getPoints());
            }
            if (top.isEmpty()) System.out.println("(empty)");
        } catch (Exception ex) {
            notificationService.notifyAdmin("showLeaderboard failed: " + ex.getMessage());
            System.out.println("Error while reading leaderboard. Contact admin.");
        }
    }

    private String promptStudentId() {
        System.out.print("Enter your student id (UUID) (or type 'list' to see students, 'cancel' to go back): ");
        String s = scanner.nextLine().trim();
        if (s.equalsIgnoreCase("cancel")) return null;
        if (s.equalsIgnoreCase("list")) {
            listStudents();
            System.out.print("Now enter student id: ");
            s = scanner.nextLine().trim();
            if (s.equalsIgnoreCase("cancel")) return null;
        }
        try {
            UUID.fromString(s);
            return s;
        } catch (IllegalArgumentException ex) {
            System.out.println("Bad UUID format.");
            return null;
        }
    }

    private void listStudents() {
        System.out.println("Students (from DB):");
        String sql = "SELECT id, name, email FROM students ORDER BY name";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.printf(" - %s | %s | %s%n", rs.getObject("id"), rs.getString("name"), rs.getString("email"));
            }
        } catch (SQLException e) {
            notificationService.notifyAdmin("listStudents failed: " + e.getMessage());
            System.out.println("Failed to list students. Contact admin.");
        }
    }
}
