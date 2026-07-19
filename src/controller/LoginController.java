package controller;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    /**
     * Authenticates the user against the database credentials.
     * Uses PreparedStatement to prevent SQL Injection attacks.
     */
    public boolean authenticate(String username, String password) {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        
        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query)
        ) {
            pst.setString(1, username);
            pst.setString(2, password);
            
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // Returns true if a matching user is found
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            return false;
        }
    }
}