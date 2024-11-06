package com.skillforge.backend.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseQuizDTO {

    private String id;
    private String title;
    private String description;
    private String courseid;
    private List<QuizDTO> questions;

}
