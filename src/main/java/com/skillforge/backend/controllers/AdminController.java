package com.skillforge.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/getAdmin")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> getAdmin(){
        return ResponseEntity.ok("Admin Access");
    }

    @PostMapping("/createCourse")
    public ResponseEntity createCourse() {
        return ResponseEntity.ok("Course Created");
    }

    @DeleteMapping("/deleteCourse")
    public ResponseEntity deleteCourse() {
        return ResponseEntity.ok("Delete Course");
    }

    @PutMapping("/assignInstructor")
    public ResponseEntity assignInstructorToCourse() {
        return ResponseEntity.ok("Assigned Instructor To Course");
    }

    @PutMapping("/addStudentToCourse")
    public ResponseEntity addStudentToCourse() {
        return ResponseEntity.ok("Added Student To Course");
    }

    @DeleteMapping("/deleteStudentFromCourse")
    public ResponseEntity deleteStudentFromCourse() {
        return ResponseEntity.ok("Student deleted From Course");
    }


}
