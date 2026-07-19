package model;

public class Student {
    private int studentId;
    private String registrationNumber;
    private String fullName;
    private int age;
    private int departmentId;
    private double gpa;

    // Constructor
    public Student(int studentId, String registrationNumber, String fullName, int age, int departmentId, double gpa) {
        this.studentId = studentId;
        this.registrationNumber = registrationNumber;
        this.fullName = fullName;
        this.age = age;
        this.departmentId = departmentId;
        this.gpa = gpa;
    }

    // Getters and Setters
    public int getStudentId() { return studentId; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getFullName() { return fullName; }
    public int getAge() { return age; }
    public int getDepartmentId() { return departmentId; }
    public double getGpa() { return gpa; }
}