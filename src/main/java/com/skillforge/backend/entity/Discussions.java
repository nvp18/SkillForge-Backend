package com.skillforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discussions")
public class Discussions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdat;

    @Column(name = "createdby", nullable = false)
    private String createdby;

    @ManyToOne
    @JoinColumn(name = "courseid",nullable = false)
    private Course course;

    @OneToMany(mappedBy = "discussions", cascade = CascadeType.REMOVE)
    private List<DiscussionReply> discussionReplyList;

}
