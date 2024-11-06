package com.skillforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "option1",nullable = false)
    private String option1;

    @Column(name = "option2",nullable = false)
    private String option2;

    @Column(name = "option3",nullable = false)
    private String option3;

    @Column(name = "option4", nullable = false)
    private String option4;

    @Column(name = "correctans" , nullable = false)
    private String correctans;

    @ManyToOne
    @JoinColumn(name = "coursequizid", nullable = false)
    private CourseQuiz coursequiz;


}
