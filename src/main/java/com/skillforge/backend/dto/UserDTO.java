package com.skillforge.backend.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;

}
