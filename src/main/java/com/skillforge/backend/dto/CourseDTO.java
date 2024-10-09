package com.skillforge.backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private String createdAt;
    private String updatedAt;

    private String courseDirectory;

    private List<String> courseFileDetails;

}
