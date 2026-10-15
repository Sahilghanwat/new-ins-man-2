package com.example.institute.dto;
import com.example.institute.entity.Enrollment;
import com.example.institute.entity.Trainer;
import java.time.LocalDateTime;
public record CourseView(Long trainerId, String courseName, String trainerName, String className, int experience, boolean enrolled, LocalDateTime enrolledAt) {
    public static CourseView catalog(Trainer t, boolean enrolled) { return new CourseView(t.getId(), t.getCourseName(), t.getName(), t.getClassName(), t.getExperience(), enrolled, null); }
    public static CourseView enrolled(Enrollment e) { Trainer t=e.getTrainer(); return new CourseView(t.getId(),t.getCourseName(),t.getName(),t.getClassName(),t.getExperience(),true,e.getEnrolledAt()); }
}
