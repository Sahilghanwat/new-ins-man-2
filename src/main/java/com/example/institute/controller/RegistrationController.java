package com.example.institute.controller;
import com.example.institute.dto.*; import com.example.institute.service.InstituteService; import jakarta.validation.Valid;
import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api")
public class RegistrationController {
 private final InstituteService service; public RegistrationController(InstituteService service){this.service=service;}
 @PostMapping("/students/register") @ResponseStatus(HttpStatus.CREATED) public StudentView registerStudent(@Valid @RequestBody StudentRequest r){return service.registerStudent(r);}
 @PostMapping("/trainers/register") @ResponseStatus(HttpStatus.CREATED) public TrainerView registerTrainer(@Valid @RequestBody TrainerRequest r){return service.registerTrainer(r);}
}
