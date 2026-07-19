package model;

public class Lecturer {
    private int lecturerId;
    private String staffNumber;
    private String fullName;
    private String email;
    private int departmentId;

    public Lecturer(int lecturerId, String staffNumber, String fullName, String email, int departmentId) {
        this.lecturerId = lecturerId;
        this.staffNumber = staffNumber;
        this.fullName = fullName;
        this.email = email;
        this.departmentId = departmentId;
    }

    // Getters
    public int getLecturerId() { return lecturerId; }
    public String getStaffNumber() { return staffNumber; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public int getDepartmentId() { return departmentId; }
}