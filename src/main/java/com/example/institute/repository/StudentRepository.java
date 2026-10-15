package com.example.institute.repository;
import com.example.institute.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.Optional;
public interface StudentRepository extends JpaRepository<Student,Long> {
 Optional<Student> findByEmailIgnoreCase(String email);
 boolean existsByEmailIgnoreCase(String email);
 List<Student> findByTrainerId(Long trainerId);
}
