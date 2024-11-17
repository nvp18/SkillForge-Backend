package com.skillforge.backend.repository;

import com.skillforge.backend.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    Quiz findById(String quizId);

    @Query(value = """
            SELECT e FROM Quiz e where e.coursequiz.id = :courseQuizId
            """)
    List<Quiz> findByCourseQuizId(String courseQuizId);
}
