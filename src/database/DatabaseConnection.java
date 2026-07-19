package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConnection {
    
    // URL pointing to the new university_db schema
    private static final String URL = "jdbc:mysql://localhost:3306/university_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Default Laragon password is blank

    /**
     * Establishes and returns a connection to the MySQL database.
     * Reusable across all components in the MVC system.
     */
    public static Connection getConnection() {
        Connection con = null;
        try {
            // Load Driver Class
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish Connection
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("[DATABASE ERROR] Connection failed: " + e.getMessage());
        }
        return con;
    }

    /**
     * Structural utility to grab Key-Value pairs for UI dropdown selection components (JComboBox).
     * Safely closes resources and uses the existing getConnection routine.
     */
    public static Map<Integer, String> getDropdownData(String tableName, String idColumn, String nameColumn) {
        Map<Integer, String> map = new HashMap<>();
        String query = "SELECT " + idColumn + ", " + nameColumn + " FROM " + tableName;
        
        Connection con = getConnection();
        if (con == null) return map;

        try (PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getInt(idColumn), rs.getString(nameColumn));
            }
        } catch (SQLException e) {
            System.err.println("[DATABASE ERROR] Failed to fetch dropdown data for " + tableName + ": " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("[DATABASE ERROR] Failed to close connection: " + e.getMessage());
            }
        }
        return map;
    }
}