package com.skillforge.backend.repository;

import com.skillforge.backend.entity.CourseQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseQuizRepository extends JpaRepository<CourseQuiz, UUID> {

    CourseQuiz findByCourseCourseid(String courseId);

    CourseQuiz findById(String id);
}
