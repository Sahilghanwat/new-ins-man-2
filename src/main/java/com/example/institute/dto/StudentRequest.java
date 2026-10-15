package com.example.institute.dto;
import jakarta.validation.constraints.*;
public record StudentRequest(@NotBlank String name, @NotBlank String city, @NotBlank @Email String email,
 @Size(max=100) String password, @NotBlank String courseName, @NotNull Long trainerId) {}
