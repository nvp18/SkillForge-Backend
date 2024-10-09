package com.skillforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employeecourse")
public class EmployeeCourses {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "assignedat", nullable = false)
    private LocalDateTime assignedAt;

    @Column(name = "duedate", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "status",nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "employeeid", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "courseid", nullable = false)
    private Course course;

}
