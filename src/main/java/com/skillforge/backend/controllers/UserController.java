package com.skillforge.backend.controllers;

import com.skillforge.backend.dto.ChangePasswordDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.UserDTO;
import com.skillforge.backend.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
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

    @GetMapping("/viewProfile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    public ResponseEntity<UserDTO> getUserProfile(Principal connectedUser) {
        UserDTO userDTO = userService.getUserProfile(connectedUser);
        return ResponseEntity.ok().body(userDTO);
    }

    @PutMapping("/updateProfile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    public ResponseEntity<UserDTO> updateUserProfile(@RequestBody UserDTO userDTO, Principal connectedUser) {
        UserDTO updatedUser = userService.updateProfile(userDTO,connectedUser);
        return ResponseEntity.ok().body(updatedUser);
    }

    @PutMapping("/changePassword")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    public ResponseEntity<GenericDTO> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, Principal connectedUser) {
        GenericDTO genericDTO = userService.changePassword(changePasswordDTO,connectedUser);
        return ResponseEntity.ok().body(genericDTO);
    }

    @GetMapping("/getAllEmployees")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllEmployees() {
        List<UserDTO> userDTOS = userService.getAllEmployees();
        return ResponseEntity.ok().body(userDTOS);
    }
}
