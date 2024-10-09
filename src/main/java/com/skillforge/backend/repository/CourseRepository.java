package com.skillforge.backend.repository;

import com.skillforge.backend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    Course findByCourseid(String courseId);
}
