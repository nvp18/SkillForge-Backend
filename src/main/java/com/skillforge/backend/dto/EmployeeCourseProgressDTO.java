package com.skillforge.backend.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeCourseProgressDTO {

    private String id;

    private String moduleId;

    private Boolean isCompleted;
}
