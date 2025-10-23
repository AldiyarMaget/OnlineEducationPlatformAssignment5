package org.example;

public class DBRunner {
    final static private String address = "jdbc:postgresql://localhost:5432/DBOnlineEducationPlatformAssignment5";
    final static private String men = "postgres";
    final static private String password = "aldiar1432";

    public static void run(){
    ConsoleApp app = new ConsoleApp(address, men, password);
    app.run();}
}
