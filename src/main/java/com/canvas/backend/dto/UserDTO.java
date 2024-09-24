package com.canvas.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;

}
