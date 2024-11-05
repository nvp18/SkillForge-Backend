package com.skillforge.backend.repository;

import com.skillforge.backend.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    List<Quiz> findByCourseCourseid(String courseId);

    Quiz findById(String quizId);
}
