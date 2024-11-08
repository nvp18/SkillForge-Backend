package com.skillforge.backend.dto;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeCourseDTO {

    private String id;
    private String assignedAt;
    private String dueDate;
    private String status;
    private Boolean quizcompleted;
    private CourseDTO course;

}
