package com.skillforge.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @GetMapping("/getAllStudents")
    //@PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<String> getAllStudents(HttpServletRequest request) {
        String sessionId= request.getSession().getId();
        return ResponseEntity.ok(sessionId);
    }

    @GetMapping("/getHello")
    //@PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<String> getHello() {
        return ResponseEntity.ok("Hello");
    }

    @PostMapping("/post")
    //@PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<String> getHiPost() {
        return  ResponseEntity.ok("Post");
    }


}
