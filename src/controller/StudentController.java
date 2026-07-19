package controller;

import database.DatabaseConnection;
import model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentController {

    // CREATE (Add Student)
    public boolean addStudent(Student student) {
        String query = "INSERT INTO Students (registration_number, full_name, age, department_id, gpa) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, student.getRegistrationNumber());
            pst.setString(2, student.getFullName());
            pst.setInt(3, student.getAge());
            if (student.getDepartmentId() > 0) pst.setInt(4, student.getDepartmentId());
            else pst.setNull(4, Types.INTEGER);
            pst.setDouble(5, student.getGpa());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ & SEARCH
    public List<Student> getAllStudents(String searchKeyword) {
        List<Student> list = new ArrayList<>();
        String query = "SELECT * FROM Students WHERE full_name LIKE ? OR registration_number LIKE ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, "%" + searchKeyword + "%");
            pst.setString(2, "%" + searchKeyword + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("registration_number"),
                        rs.getString("full_name"),
                        rs.getInt("age"),
                        rs.getInt("department_id"),
                        rs.getDouble("gpa")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public boolean updateStudent(Student student) {
        String query = "UPDATE Students SET full_name = ?, age = ?, gpa = ? WHERE registration_number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, student.getFullName());
            pst.setInt(2, student.getAge());
            pst.setDouble(3, student.getGpa());
            pst.setString(4, student.getRegistrationNumber());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteStudent(String regNum) {
        String query = "DELETE FROM Students WHERE registration_number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, regNum);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}