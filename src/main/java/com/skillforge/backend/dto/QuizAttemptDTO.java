package com.skillforge.backend.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttemptDTO {

    private String id;
    private String attemptedAns;
}
