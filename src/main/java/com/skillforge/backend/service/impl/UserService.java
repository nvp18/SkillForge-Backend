package com.skillforge.backend.service.impl;

import com.skillforge.backend.config.JwtService;
import com.skillforge.backend.dto.ChangePasswordDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.UserDTO;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.exception.*;
import com.skillforge.backend.repository.UserRepository;
import com.skillforge.backend.service.UserInterface;
import com.skillforge.backend.utils.ObjectMappers;
import com.skillforge.backend.utils.ROLES;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

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
    public Map<String,Object> login(String userName, String password) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userName, password));
            if (authentication.isAuthenticated()) {
                Map<String,Object> user = new HashMap<>();
                String role = repository.findUserRole(userName);
                String jwtToken = jwtService.generateToken(userName);
                user.put("Role", role);
                user.put("Token", jwtToken);
                return user;
            } else {
                throw new UserNotAuthenticatedException();
            }
        } catch (BadCredentialsException b) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        try {
            User user = new User();
            String randomPassword = generatePassword();
            user.setUsername(userDTO.getUserName());
            user.setPassword(encoder.encode(randomPassword));
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setRole(ROLES.EMPLOYEE.toString());
            User savedUser = repository.save(user);
            userDTO.setUserId(savedUser.getUserId());
            userDTO.setRole(savedUser.getRole());
            userDTO.setPassword(randomPassword);
            return userDTO;
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public UserDTO getUserProfile(Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            if(user == null) {
                throw new ResourceNotFoundException();
            }
            return ObjectMappers.userToUserDTO(user);
        } catch (Exception e) {
            if(e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException();
            }
            throw new InternalServerException();
        }
    }

    @Override
    public UserDTO updateProfile(UserDTO userDTO, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            User savedUser = repository.save(user);
            return ObjectMappers.userToUserDTO(savedUser);
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO changePassword(ChangePasswordDTO changePasswordDTO, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            if(!encoder.matches(changePasswordDTO.getCurrentPassword(),user.getPassword())) {
                throw new GenericException();
            }
            if(!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
                throw new GenericException();
            }
            user.setPassword(encoder.encode(changePasswordDTO.getNewPassword()));
            repository.save(user);
            return GenericDTO.builder()
                    .message("Password Changed Successfully")
                    .build();
        } catch (Exception e) {
            if(e instanceof GenericException) {
                throw new GenericException();
            }
            throw new InternalServerException();
        }
    }

    private String generatePassword() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_.";
        String password = RandomStringUtils.random( 15, characters );
        return password;
    }
}
