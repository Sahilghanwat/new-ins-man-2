package com.example.institute.search;

/** Search-only representation of a student. Never includes the password. */
public class StudentSearchDocument {
    private Long studentId;
    private String name;
    private String city;
    private String email;
    private String course;
    private Long trainerId;
    private String trainerName;
    private String role;

    public StudentSearchDocument() {}

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public Long getTrainerId() { return trainerId; }
    public void setTrainerId(Long trainerId) { this.trainerId = trainerId; }
    public String getTrainerName() { return trainerName; }
    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
