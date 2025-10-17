package org.example;

public class AppLauncher {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/DBOnlineEducationPlatformAssignment5";
        String user = "postgres";
        String password = "aldiar1432";

        ConsoleApp app = new ConsoleApp(jdbcUrl, user, password);
        app.run();
    }
}
