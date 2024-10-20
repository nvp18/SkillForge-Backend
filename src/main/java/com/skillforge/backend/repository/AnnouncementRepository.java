package com.skillforge.backend.repository;

import com.skillforge.backend.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, UUID> {

    List<Announcement> findByCourseCourseid(String courseId);

    void deleteById(String announcementId);

    Announcement findById(String announcementId);
}
