package com.example.institute.controller;
import com.example.institute.dto.*; import com.example.institute.service.*; import jakarta.servlet.http.HttpServletRequest; import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/trainers")
public class TrainerController {
 private final InstituteService service; private final SessionService sessions; public TrainerController(InstituteService service,SessionService sessions){this.service=service;this.sessions=sessions;}
 @GetMapping("/me") public TrainerView me(HttpServletRequest r){return service.trainer(sessions.requireId(r,"TRAINER"));}
 @GetMapping("/my-students") public List<StudentView> myStudents(HttpServletRequest r){return service.trainerStudents(sessions.requireId(r,"TRAINER"));}
 @GetMapping("/available") public List<TrainerView> available(){return service.allTrainers();}
 @GetMapping public List<TrainerView> list(HttpServletRequest r){sessions.requireAdmin(r);return service.allTrainers();}
 @PutMapping("/{id}") public TrainerView update(@PathVariable Long id,@Valid @RequestBody TrainerRequest body,HttpServletRequest r){sessions.requireAdmin(r);return service.updateTrainer(id,body);}
 @DeleteMapping("/{id}") public Map<String,String> delete(@PathVariable Long id,HttpServletRequest r){sessions.requireAdmin(r);service.deleteTrainer(id);return Map.of("message","Trainer deleted.");}
}
