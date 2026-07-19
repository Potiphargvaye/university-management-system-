package controller;

import database.DatabaseConnection;
import model.Lecturer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerController {

    // CREATE
    public boolean addLecturer(Lecturer lecturer) {
        String query = "INSERT INTO Lecturers (staff_number, full_name, email, department_id) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, lecturer.getStaffNumber());
            pst.setString(2, lecturer.getFullName());
            pst.setString(3, lecturer.getEmail());
            if (lecturer.getDepartmentId() > 0) pst.setInt(4, lecturer.getDepartmentId());
            else pst.setNull(4, Types.INTEGER);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ & SEARCH
    public List<Lecturer> getAllLecturers(String searchKeyword) {
        List<Lecturer> list = new ArrayList<>();
        String query = "SELECT * FROM Lecturers WHERE full_name LIKE ? OR staff_number LIKE ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, "%" + searchKeyword + "%");
            pst.setString(2, "%" + searchKeyword + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new Lecturer(
                        rs.getInt("lecturer_id"),
                        rs.getString("staff_number"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getInt("department_id")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public boolean updateLecturer(Lecturer lecturer) {
        String query = "UPDATE Lecturers SET full_name = ?, email = ? WHERE staff_number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, lecturer.getFullName());
            pst.setString(2, lecturer.getEmail());
            pst.setString(3, lecturer.getStaffNumber());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteLecturer(String staffNum) {
        String query = "DELETE FROM Lecturers WHERE staff_number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, staffNum);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}