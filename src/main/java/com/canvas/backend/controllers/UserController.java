package com.canvas.backend.controllers;

import com.canvas.backend.dto.UserDTO;
import com.canvas.backend.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("userName") String userName, @RequestParam("passWord") String password) {
        String token = userService.login(userName,password);
        if(token!=null) {
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not Found");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        UserDTO registeredDTO = userService.register(userDTO);
        if(registeredDTO!=null) {
            return ResponseEntity.status(HttpStatus.OK).body("User Registered");
        } else {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not Registered");
        }
    }
}
