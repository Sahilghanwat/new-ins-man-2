package com.example.institute.dto;
import jakarta.validation.constraints.*;
public record AdminLoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
