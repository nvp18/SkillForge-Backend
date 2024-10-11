package com.skillforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "concern")
public class Concerns {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdat;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status",nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "employeeid", nullable = false)
    private User user;

    @OneToMany(mappedBy = "concerns")
    private List<ConcernReply> concernReplies = new ArrayList<>();


}
