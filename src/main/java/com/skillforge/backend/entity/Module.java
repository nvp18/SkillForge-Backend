package com.skillforge.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Module")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String moduleid;

    @Column(name = "modulename", nullable = false)
    private String moduleName;

    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedat", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "modulecontent", nullable = false)
    private String modulecontent;

    @Column(name = "modulenumber", nullable = false)
    private Integer modulenumber;

    @ManyToOne
    @JoinColumn(name = "courseid")
    private Course course;

    @OneToMany(mappedBy = "module",cascade =  CascadeType.REMOVE)
    private List<EmployeeCourseProgress> employeeCourseProgressList;

}
