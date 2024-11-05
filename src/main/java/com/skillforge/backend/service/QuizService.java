package com.skillforge.backend.service;

import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.QuizAttemptDTO;
import com.skillforge.backend.dto.QuizDTO;

import java.security.Principal;
import java.util.List;

public interface QuizService {

    GenericDTO createQuiz(String courseId,List<QuizDTO> quizDTOs);

    GenericDTO deleteQuestion(String questionId);

    List<QuizDTO> getCourseQuiz(String courseId);

    List<QuizDTO> getCourseQuizForEmployee(String courseId, Principal connectedUser);

    GenericDTO attemptQuiz(String courseId, Principal connectedUser, List<QuizAttemptDTO> quizAttemptDTOS);
}
