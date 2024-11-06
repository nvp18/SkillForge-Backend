package com.skillforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employeecourseprogress")
public class EmployeeCourseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "employeecourseid")
    private EmployeeCourses employeeCourses;

    @ManyToOne
    @JoinColumn(name = "moduleid")
    private Module module;

    @Column(name = "iscompleted",nullable = false)
    private Boolean isCompleted;


}
