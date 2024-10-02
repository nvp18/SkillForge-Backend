package com.skillforge.backend.service;

import com.skillforge.backend.dto.UserDTO;

public interface UserInterface {
    String login(String userName,String password);
    UserDTO createUser(UserDTO userDTO);

}
