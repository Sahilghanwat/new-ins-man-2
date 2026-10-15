package com.example.institute.service;
import com.example.institute.dto.*; import com.example.institute.entity.*; import com.example.institute.repository.*; import com.example.institute.search.StudentSearchService;
import org.springframework.stereotype.Service; import org.springframework.http.HttpStatus; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.web.server.ResponseStatusException;
import java.util.*;
@Service
public class InstituteService {
 private final StudentRepository students; private final TrainerRepository trainers; private final PasswordEncoder encoder; private final StudentSearchService studentSearch;
 public InstituteService(StudentRepository students,TrainerRepository trainers,PasswordEncoder encoder,StudentSearchService studentSearch){this.students=students;this.trainers=trainers;this.encoder=encoder;this.studentSearch=studentSearch;}
 public StudentView registerStudent(StudentRequest r){
  if(r.password()==null || r.password().length()<8) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Password must contain at least 8 characters.");
  if(students.existsByEmailIgnoreCase(r.email())) throw new ResponseStatusException(HttpStatus.CONFLICT,"A student with this email already exists.");
  Trainer t=trainers.findById(r.trainerId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Selected trainer does not exist."));
  Student s=new Student(); s.setName(r.name().trim()); s.setCity(r.city().trim()); s.setEmail(r.email().trim().toLowerCase()); s.setPassword(encoder.encode(r.password())); s.setCourseName(r.courseName().trim()); s.setTrainer(t); s.setRole("STUDENT"); Student saved=students.save(s); studentSearch.index(saved); return StudentView.from(saved);
 }
 public TrainerView registerTrainer(TrainerRequest r){
  if(r.password()==null || r.password().length()<8) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Password must contain at least 8 characters.");
  if(trainers.existsByEmailIgnoreCase(r.email())) throw new ResponseStatusException(HttpStatus.CONFLICT,"A trainer with this email already exists.");
  if(trainers.existsByEmployeeNumberIgnoreCase(r.employeeNumber())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Employee number already exists.");
  Trainer t=new Trainer(); apply(t,r,false); return TrainerView.from(trainers.save(t));
 }
 private void apply(Trainer t,TrainerRequest r,boolean keepPassword){t.setName(r.name().trim());t.setClassName(r.className().trim());t.setCourseName(r.courseName().trim());t.setExperience(r.experience());t.setEmail(r.email().trim().toLowerCase());t.setEmployeeNumber(r.employeeNumber().trim());if(!keepPassword||r.password()!=null&&!r.password().isBlank())t.setPassword(encoder.encode(r.password()));t.setRole("TRAINER");}
 public StudentView studentLogin(LoginRequest r){Student s=students.findByEmailIgnoreCase(r.email()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password."));if(!encoder.matches(r.password(),s.getPassword()))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password.");return StudentView.from(s);}
 public TrainerView trainerLogin(LoginRequest r){Trainer t=trainers.findByEmailIgnoreCase(r.email()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password."));if(!encoder.matches(r.password(),t.getPassword()))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password.");return TrainerView.from(t);}
 public StudentView student(Long id){return StudentView.from(students.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found.")));}
 public TrainerView trainer(Long id){return TrainerView.from(trainers.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Trainer not found.")));}
 public List<StudentView> allStudents(){return students.findAll().stream().map(StudentView::from).toList();}
 public List<TrainerView> allTrainers(){return trainers.findAll().stream().map(TrainerView::from).toList();}
 public List<StudentView> trainerStudents(Long id){return students.findByTrainerId(id).stream().map(StudentView::from).toList();}
 public StudentView updateStudent(Long id,StudentRequest r){Student s=students.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found."));students.findByEmailIgnoreCase(r.email()).filter(existing->!existing.getId().equals(id)).ifPresent(existing->{throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already belongs to another student.");});Trainer t=trainers.findById(r.trainerId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Selected trainer does not exist."));s.setName(r.name().trim());s.setCity(r.city().trim());s.setEmail(r.email().trim().toLowerCase());s.setCourseName(r.courseName().trim());s.setTrainer(t);if(r.password()!=null&&!r.password().isBlank())s.setPassword(encoder.encode(r.password()));s.setRole("STUDENT");Student saved=students.save(s);studentSearch.index(saved);return StudentView.from(saved);}
 public TrainerView updateTrainer(Long id,TrainerRequest r){Trainer t=trainers.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Trainer not found."));trainers.findByEmailIgnoreCase(r.email()).filter(existing->!existing.getId().equals(id)).ifPresent(existing->{throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already belongs to another trainer.");});if(trainers.existsByEmployeeNumberIgnoreCase(r.employeeNumber())&&!t.getEmployeeNumber().equalsIgnoreCase(r.employeeNumber()))throw new ResponseStatusException(HttpStatus.CONFLICT,"Employee number already exists.");apply(t,r,true);return TrainerView.from(trainers.save(t));}
 public void deleteStudent(Long id){if(!students.existsById(id))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found.");students.deleteById(id);studentSearch.delete(id);}
 public void deleteTrainer(Long id){if(!trainers.existsById(id))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Trainer not found.");if(!students.findByTrainerId(id).isEmpty())throw new ResponseStatusException(HttpStatus.CONFLICT,"Reassign or delete this trainer's students before deleting the trainer.");trainers.deleteById(id);}
}
