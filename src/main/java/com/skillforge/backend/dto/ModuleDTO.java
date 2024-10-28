package com.skillforge.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModuleDTO {

    private String moduleId;
    private String moduleName;

    private int moduleNumber;

}
