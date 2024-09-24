package com.canvas.backend.service.impl;

import com.canvas.backend.config.JwtService;
import com.canvas.backend.dto.UserDTO;
import com.canvas.backend.entity.User;
import com.canvas.backend.repository.UserRepository;
import com.canvas.backend.service.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserInterface {

    @Autowired
    UserRepository repository;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public String login(String userName, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userName,password));
        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(userName);
        } else {
            return "False";
        }
    }

    @Override
    public UserDTO register(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUserName());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setRole("STUDENT");
        User savedUser = repository.save(user);
        userDTO.setUserId(savedUser.getUserId());
        userDTO.setRole(savedUser.getRole());
        return userDTO;
    }
}
