package com.example.institute.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="trainers")
public class Trainer {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @NotBlank @Column(nullable=false) private String name;
  @NotBlank @Column(nullable=false) private String className;
  @NotBlank @Column(nullable=false) private String courseName;
  @Min(0) private int experience;
  @NotBlank @Email @Column(nullable=false, unique=true) private String email;
  @NotBlank @Column(nullable=false, unique=true) private String employeeNumber;
  @Column(nullable=false) private String password;
  @Column(nullable=false) private String role="TRAINER";
  public Trainer() {}
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getName(){return name;} public void setName(String name){this.name=name;}
  public String getClassName(){return className;} public void setClassName(String className){this.className=className;}
  public String getCourseName(){return courseName;} public void setCourseName(String courseName){this.courseName=courseName;}
  public int getExperience(){return experience;} public void setExperience(int experience){this.experience=experience;}
  public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
  public String getEmployeeNumber(){return employeeNumber;} public void setEmployeeNumber(String employeeNumber){this.employeeNumber=employeeNumber;}
  public String getPassword(){return password;} public void setPassword(String password){this.password=password;}
  public String getRole(){return role;} public void setRole(String role){this.role=role;}
}
