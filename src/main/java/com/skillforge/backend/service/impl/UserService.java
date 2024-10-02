package com.skillforge.backend.service.impl;

import com.skillforge.backend.config.JwtService;
import com.skillforge.backend.dto.UserDTO;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.repository.UserRepository;
import com.skillforge.backend.service.UserInterface;
import com.skillforge.backend.utils.ROLES;
import org.apache.commons.lang3.RandomStringUtils;
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
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        String randomPassword = generatePassword();
        user.setUsername(userDTO.getUserName());
        user.setPassword(encoder.encode(randomPassword));
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setRole(ROLES.STUDENT.toString());
        User savedUser = repository.save(user);
        userDTO.setUserId(savedUser.getUserId());
        userDTO.setRole(savedUser.getRole());
        userDTO.setPassword(randomPassword);
        return userDTO;
    }

    private String generatePassword() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_.";
        String password = RandomStringUtils.random( 15, characters );
        return password;
    }
}
