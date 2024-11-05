package com.skillforge.backend.repository;

import com.skillforge.backend.entity.DiscussionReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DiscussionReplyRepository extends JpaRepository<DiscussionReply, UUID> {
}
