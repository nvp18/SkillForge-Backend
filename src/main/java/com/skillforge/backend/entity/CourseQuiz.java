package com.skillforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coursequiz")
public class CourseQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description",nullable = false)
    private String description;

    @OneToOne
    @JoinColumn(name = "courseid",nullable = false)
    private Course course;

    @OneToMany(mappedBy = "coursequiz", cascade = CascadeType.REMOVE)
    List<Quiz> quizzes = new ArrayList<>();

}
