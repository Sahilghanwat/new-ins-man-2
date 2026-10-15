package com.example.institute.repository;
import com.example.institute.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface TrainerRepository extends JpaRepository<Trainer,Long> {
 Optional<Trainer> findByEmailIgnoreCase(String email);
 boolean existsByEmailIgnoreCase(String email);
 boolean existsByEmployeeNumberIgnoreCase(String employeeNumber);
}
