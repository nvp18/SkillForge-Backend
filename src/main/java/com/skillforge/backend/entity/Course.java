package com.skillforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "courseid")
    private String courseid;

    @Column(name = "coursename", nullable = false, unique = true)
    private String courseName;

    @Column(name = "coursedescription", nullable = false)
    private String courseDescription;

    @Column(name = "coursetags", nullable = false)
    private String courseTags;

    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedat", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "daystofinish", nullable = false)
    private Integer days;

    @Column(name = "coursedirectory", nullable = false, unique = true)
    private String courseDirectory;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Module> courseModules;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Announcement> announcements;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Discussions> discussions;

    @OneToOne(mappedBy = "course",cascade = CascadeType.REMOVE)
    private CourseQuiz courseQuiz;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<EmployeeCourses> employeeCourses = new ArrayList<>();

}
