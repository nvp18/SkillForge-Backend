package com.canvas.backend.service;

import com.canvas.backend.dto.UserDTO;

public interface UserInterface {
    String login(String userName,String password);
    UserDTO register(UserDTO userDTO);

}
