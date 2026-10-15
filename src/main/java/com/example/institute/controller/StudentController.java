package com.example.institute.controller;
import com.example.institute.dto.StudentView;
import com.example.institute.service.*;
import com.example.institute.search.StudentSearchService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController @RequestMapping("/api/students")
public class StudentController {
 private final InstituteService service;
 private final SessionService sessions;
 private final StudentSearchService studentSearch;
 public StudentController(InstituteService service,SessionService sessions,StudentSearchService studentSearch){this.service=service;this.sessions=sessions;this.studentSearch=studentSearch;}
 @GetMapping("/me") public StudentView me(HttpServletRequest r){return service.student(sessions.requireId(r,"STUDENT"));}
 @GetMapping public List<StudentView> list(HttpServletRequest r){sessions.requireAdmin(r);return service.allStudents();}
 @GetMapping("/search") public List<StudentView> search(@RequestParam("q") String q,HttpServletRequest r){sessions.requireAdmin(r);return studentSearch.search(q);}
 @PostMapping("/search/reindex") public Map<String,Object> reindex(HttpServletRequest r){sessions.requireAdmin(r);int count=studentSearch.reindexAll();return Map.of("message","Student search index rebuilt from MySQL.","indexedCount",count);}
 @PutMapping("/{id}") public StudentView update(@PathVariable Long id,@jakarta.validation.Valid @RequestBody com.example.institute.dto.StudentRequest body,HttpServletRequest r){sessions.requireAdmin(r);return service.updateStudent(id,body);}
 @DeleteMapping("/{id}") public Map<String,String> delete(@PathVariable Long id,HttpServletRequest r){sessions.requireAdmin(r);service.deleteStudent(id);return Map.of("message","Student deleted.");}
}
