package com.skillforge.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CourseDTO {

    private String courseId;
    private String courseName;
    private String courseDescription;
    private String courseTags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
