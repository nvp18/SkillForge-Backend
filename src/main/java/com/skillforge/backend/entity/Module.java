package com.skillforge.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Module")
public class Module {

    @Id
    private String moduleId;

    @Column(name = "modulename", nullable = false)
    private String moduleName;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "modulecontent", nullable = false)
    private String modulecontent;

    @ManyToOne
    @JoinColumn(name = "courseid")
    private Course course;

}
