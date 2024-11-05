package com.skillforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discussionreply")
public class DiscussionReply {

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
    @JoinColumn(name = "discussionid",nullable = false)
    private Discussions discussions;
}
