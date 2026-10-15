package com.example.institute.controller;

import com.example.institute.dto.CourseView;
import com.example.institute.entity.Enrollment;
import com.example.institute.entity.Student;
import com.example.institute.entity.Trainer;
import com.example.institute.repository.EnrollmentRepository;
import com.example.institute.repository.StudentRepository;
import com.example.institute.repository.TrainerRepository;
import com.example.institute.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final SessionService sessions; private final StudentRepository students; private final TrainerRepository trainers; private final EnrollmentRepository enrollments;
    public CourseController(SessionService sessions, StudentRepository students, TrainerRepository trainers, EnrollmentRepository enrollments) { this.sessions=sessions; this.students=students; this.trainers=trainers; this.enrollments=enrollments; }

    @GetMapping public List<CourseView> catalog(HttpServletRequest request) {
        Long studentId=sessions.requireId(request,"STUDENT");
        return trainers.findAll().stream().map(t -> CourseView.catalog(t,enrollments.existsByStudentIdAndTrainerId(studentId,t.getId()))).toList();
    }
    @GetMapping("/my") public List<CourseView> myCourses(HttpServletRequest request) {
        Long studentId=sessions.requireId(request,"STUDENT");
        return enrollments.findByStudentIdOrderByEnrolledAtDesc(studentId).stream().map(CourseView::enrolled).toList();
    }
    @PostMapping("/{trainerId}/enroll") @ResponseStatus(HttpStatus.CREATED)
    public CourseView enroll(@PathVariable Long trainerId, HttpServletRequest request) {
        Long studentId=sessions.requireId(request,"STUDENT");
        if(enrollments.existsByStudentIdAndTrainerId(studentId,trainerId)) throw new ResponseStatusException(HttpStatus.CONFLICT,"You are already enrolled in this course.");
        Student student=students.findById(studentId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found."));
        Trainer trainer=trainers.findById(trainerId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Course or trainer not found."));
        Enrollment enrollment=new Enrollment(); enrollment.setStudent(student); enrollment.setTrainer(trainer);
        return CourseView.enrolled(enrollments.save(enrollment));
    }
}
