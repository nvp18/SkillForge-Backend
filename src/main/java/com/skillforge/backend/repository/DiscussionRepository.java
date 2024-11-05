package com.skillforge.backend.repository;

import com.skillforge.backend.entity.Discussions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DiscussionRepository extends JpaRepository<Discussions, UUID> {

    List<Discussions> findByCourseCourseid(String courseId);

    Discussions findById(String discussionId);
}
