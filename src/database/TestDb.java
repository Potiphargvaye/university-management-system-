package database;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDb {
    public static void main(String[] args) {
        System.out.println("Testing system connection to university_db...");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("[SUCCESS] Task 1 Completed! Reusable connection works flawlessly.");
            } else {
                System.out.println("[FAILED] Connection returned null or closed.");
            }
        } catch (SQLException e) {
            System.err.println("Test execution error: " + e.getMessage());
        }
    }
}