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
@Table(name = "concernreply")
public class ConcernReply {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "repliedby", nullable = false)
    private String repliedBy;

    @Column(name = "reply", nullable = false)
    private String reply;

    @Column(name = "repliedat",nullable = false)
    private LocalDateTime repliedat;

    @ManyToOne
    @JoinColumn(name = "concernid", nullable = false)
    private Concerns concerns;
}
