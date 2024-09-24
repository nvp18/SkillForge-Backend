package com.canvas.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/getAdmin")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> getAdmin(){
        return ResponseEntity.ok("Admin Access");
    }


}
