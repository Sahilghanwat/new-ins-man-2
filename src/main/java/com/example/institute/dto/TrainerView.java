package com.example.institute.dto;
import com.example.institute.entity.Trainer;
public record TrainerView(Long id,String name,String className,String courseName,int experience,String email,String employeeNumber,String role) {
 public static TrainerView from(Trainer t){return new TrainerView(t.getId(),t.getName(),t.getClassName(),t.getCourseName(),t.getExperience(),t.getEmail(),t.getEmployeeNumber(),t.getRole());}
}
