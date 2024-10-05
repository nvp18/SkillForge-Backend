package com.skillforge.backend.service;

import com.skillforge.backend.dto.UserDTO;

import java.util.Map;

public interface UserInterface {
    Map<String,Object> login(String userName, String password);
    UserDTO createUser(UserDTO userDTO);

}
