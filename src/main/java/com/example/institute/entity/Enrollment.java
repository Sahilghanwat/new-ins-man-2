package com.example.institute.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_enrollments", uniqueConstraints = @UniqueConstraint(name = "uk_student_trainer_course", columnNames = {"student_id", "trainer_id"}))
public class Enrollment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "student_id", nullable = false) private Student student;
    @ManyToOne(fetch = FetchType.EAGER, optional = false) @JoinColumn(name = "trainer_id", nullable = false) private Trainer trainer;
    @Column(nullable = false) private LocalDateTime enrolledAt = LocalDateTime.now();
    public Enrollment() {}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Student getStudent(){return student;} public void setStudent(Student student){this.student=student;}
    public Trainer getTrainer(){return trainer;} public void setTrainer(Trainer trainer){this.trainer=trainer;}
    public LocalDateTime getEnrolledAt(){return enrolledAt;} public void setEnrolledAt(LocalDateTime enrolledAt){this.enrolledAt=enrolledAt;}
}
