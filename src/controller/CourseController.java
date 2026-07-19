package controller;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CourseController {

    // ==========================================
    // 🏢 DEPARTMENT CRUD & STATS OPERATIONS
    // ==========================================
    public boolean addDepartment(String name) {
        String query = "INSERT INTO Departments (department_name) VALUES (?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, name);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, 
                "SQL Error [Add Department]: " + e.getMessage() + "\nQuery: " + query, 
                "Database Exception Caught", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); 
            return false; 
        }
    }

    public List<Object[]> getAllDepartments() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT d.department_id, d.department_name, COUNT(l.lecturer_id) AS total_staff " +
                       "FROM Departments d LEFT JOIN Lecturers l ON d.department_id = l.department_id " +
                       "GROUP BY d.department_id, d.department_name";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{ rs.getInt("department_id"), rs.getString("department_name"), rs.getInt("total_staff") });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean deleteDepartment(int id) {
        String query = "DELETE FROM Departments WHERE department_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, 
                "SQL Error [Delete Department]: " + e.getMessage(), 
                "Database Exception Caught", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); 
            return false; 
        }
    }

    // ==========================================
    // 📚 COURSE CRUD & STATS OPERATIONS
    // ==========================================
    public boolean addCourse(String code, String title, int deptId) {
        String query = "INSERT INTO Courses (course_code, course_name, department_id) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection()) {
            if (con == null) {
                JOptionPane.showMessageDialog(null, "Database connection is NULL inside addCourse!", "Connection Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, code);
                pst.setString(2, title);
                pst.setInt(3, deptId);
                return pst.executeUpdate() > 0;
            }
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, 
                "SQL Error [Add Course]: " + e.getMessage() + "\n\nQuery Used:\n" + query, 
                "Database Exception Caught", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false; 
        }
    }

    public List<Object[]> getCoursesWithStats(String keyword) {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT c.course_id, c.course_code, c.course_name, COALESCE(d.department_name, 'Unassigned') AS department_name, COUNT(e.enrollment_id) AS student_count " +
                       "FROM Courses c " +
                       "LEFT JOIN Departments d ON c.department_id = d.department_id " +
                       "LEFT JOIN Enrollments e ON c.course_id = e.course_id " +
                       "WHERE c.course_name LIKE ? OR c.course_code LIKE ? " +
                       "GROUP BY c.course_id, c.course_code, c.course_name, d.department_name";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getInt("course_id"), 
                        rs.getString("course_code"),
                        rs.getString("course_name"), 
                        rs.getString("department_name"), 
                        rs.getInt("student_count")
                    });
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public boolean deleteCourse(int id) {
        String query = "DELETE FROM Courses WHERE course_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, 
                "SQL Error [Delete Course]: " + e.getMessage(), 
                "Database Exception Caught", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); 
            return false; 
        }
    }

    // ==========================================
    // 📝 STUDENT ENROLLMENT OPERATIONS
    // ==========================================
    public boolean enrollStudent(int studentId, int courseId, String semester) {
        String query = "INSERT INTO Enrollments (student_id, course_id) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, studentId);
            pst.setInt(2, courseId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, 
                "SQL Error [Enroll Student]: " + e.getMessage() + "\nQuery: " + query, 
                "Database Exception Caught", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); 
            return false; 
        }
    }

    public List<Object[]> getAllEnrollments(String keyword) {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT e.enrollment_id, s.registration_number, s.full_name AS student_name, " +
                       "c.course_code, c.course_name, e.enrollment_date " +
                       "FROM Enrollments e " +
                       "JOIN Students s ON e.student_id = s.student_id " +
                       "JOIN Courses c ON e.course_id = c.course_id " +
                       "WHERE s.full_name LIKE ? OR c.course_name LIKE ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getInt("enrollment_id"), rs.getString("registration_number"),
                        rs.getString("student_name"), rs.getString("course_code"),
                        rs.getString("course_name"), rs.getString("enrollment_date")
                    });
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}