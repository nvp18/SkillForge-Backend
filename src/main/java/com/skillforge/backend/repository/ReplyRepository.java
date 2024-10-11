package com.skillforge.backend.repository;

import com.skillforge.backend.entity.ConcernReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReplyRepository extends JpaRepository<ConcernReply, UUID> {
}
