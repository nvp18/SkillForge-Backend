package com.skillforge.backend.dto;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {

    private String currentPassword;

    private String newPassword;

    private String confirmPassword;

}
