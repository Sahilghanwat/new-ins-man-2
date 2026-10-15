package com.example.institute.repository;
import com.example.institute.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentIdAndTrainerId(Long studentId, Long trainerId);
    List<Enrollment> findByStudentIdOrderByEnrolledAtDesc(Long studentId);
}
