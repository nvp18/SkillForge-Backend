package com.canvas.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/instructor")
public class InstructorController {

    @GetMapping("/getInstructor")
    //@PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<String> getInstructor(){
        return ResponseEntity.ok("Instructor Access");
    }
}
