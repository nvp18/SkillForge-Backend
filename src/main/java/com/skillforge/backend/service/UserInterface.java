package com.skillforge.backend.service;

import com.skillforge.backend.dto.ChangePasswordDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.UserDTO;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserInterface {
    Map<String,Object> login(String userName, String password);
    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserProfile(Principal connectedUser);

    UserDTO updateProfile(UserDTO userDTO, Principal connectedUser);

    GenericDTO changePassword(ChangePasswordDTO changePasswordDTO, Principal connectedUser);

    List<UserDTO> getAllEmployees();

}
