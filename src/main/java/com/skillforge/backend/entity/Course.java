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

    @Column(name = "coursedirectory", nullable = false, unique = true)
    private String courseDirectory;

    @OneToMany(mappedBy = "course")
    private List<EmployeeCourses> employeeCourses = new ArrayList<>();

}
