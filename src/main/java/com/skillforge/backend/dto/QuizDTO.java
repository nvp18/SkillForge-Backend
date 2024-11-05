package com.skillforge.backend.dto;

import com.skillforge.backend.entity.Course;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizDTO {

    private String id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String correctans;

}

