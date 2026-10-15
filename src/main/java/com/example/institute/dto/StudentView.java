package com.example.institute.dto;
import com.example.institute.entity.Student;
public record StudentView(Long id,String name,String city,String email,String courseName,Long trainerId,String trainerName,String role) {
 public static StudentView from(Student s){return new StudentView(s.getId(),s.getName(),s.getCity(),s.getEmail(),s.getCourseName(),s.getTrainer()==null?null:s.getTrainer().getId(),s.getTrainer()==null?null:s.getTrainer().getName(),s.getRole());}
}
