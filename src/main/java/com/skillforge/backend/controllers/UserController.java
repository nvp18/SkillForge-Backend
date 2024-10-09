package com.skillforge.backend.controllers;

import com.skillforge.backend.dto.UserDTO;
import com.skillforge.backend.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody UserDTO userDTO) {
        Map<String,Object> token = userService.login(userDTO.getUserName(),userDTO.getPassword());
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/createUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(createUser);
    }
}
