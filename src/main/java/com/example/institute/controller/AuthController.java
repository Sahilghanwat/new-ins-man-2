package com.example.institute.controller;
import com.example.institute.dto.*; import com.example.institute.service.*; import jakarta.servlet.http.*; import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value; import org.springframework.http.ResponseEntity; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final InstituteService service; private final SessionService sessions; private final String adminEmail,adminPassword;
 public AuthController(InstituteService service,SessionService sessions,@Value("${app.admin.email}") String adminEmail,@Value("${app.admin.password}") String adminPassword){this.service=service;this.sessions=sessions;this.adminEmail=adminEmail;this.adminPassword=adminPassword;}
 @PostMapping("/student/login") public StudentView studentLogin(@Valid @RequestBody LoginRequest r,HttpServletRequest req){StudentView v=service.studentLogin(r);sessions.create(req,v.id(),v.email(),"STUDENT");return v;}
 @PostMapping("/trainer/login") public TrainerView trainerLogin(@Valid @RequestBody LoginRequest r,HttpServletRequest req){TrainerView v=service.trainerLogin(r);sessions.create(req,v.id(),v.email(),"TRAINER");return v;}
 @PostMapping("/admin/login") public ResponseEntity<?> adminLogin(@Valid @RequestBody AdminLoginRequest r,HttpServletRequest req){if(!adminEmail.equalsIgnoreCase(r.email())||!adminPassword.equals(r.password()))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED,"Invalid admin email or password.");sessions.create(req,null,adminEmail,"ADMIN");return ResponseEntity.ok(Map.of("email",adminEmail,"role","ADMIN","message","Admin login successful."));}
 @PostMapping("/logout") public ResponseEntity<?> logout(HttpServletRequest req){HttpSession s=req.getSession(false);if(s!=null)s.invalidate();return ResponseEntity.ok(Map.of("message","Logged out successfully."));}
 @GetMapping("/session") public ResponseEntity<?> session(HttpServletRequest req){HttpSession s=req.getSession(false);if(s==null||s.getAttribute("role")==null)return ResponseEntity.status(401).body(Map.of("message","Not logged in."));return ResponseEntity.ok(Map.of("role",s.getAttribute("role"),"email",s.getAttribute("email")));}
}
