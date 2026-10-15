package com.example.institute.dto;
import jakarta.validation.constraints.*;
public record TrainerRequest(@NotBlank String name, @NotBlank String className, @NotBlank String courseName,
 @Min(0) int experience, @NotBlank @Email String email, @NotBlank String employeeNumber,
 @Size(max=100) String password) {}
