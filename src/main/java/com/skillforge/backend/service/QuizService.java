package com.skillforge.backend.service;

import com.skillforge.backend.dto.CourseQuizDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.QuizAttemptDTO;
import com.skillforge.backend.dto.QuizDTO;

import java.security.Principal;
import java.util.List;

public interface QuizService {

    GenericDTO createQuiz(String courseId, CourseQuizDTO courseQuizDTO);

    GenericDTO deleteQuestion(String questionId);

    CourseQuizDTO getCourseQuiz(String courseId);

    CourseQuizDTO getCourseQuizForEmployee(String courseId, Principal connectedUser);

    GenericDTO attemptQuiz(String courseId, Principal connectedUser, List<QuizAttemptDTO> quizAttemptDTOS);

    List<QuizDTO> getQuizQuestions(String quizId);

    GenericDTO deleteQuiz(String quizId);
}
