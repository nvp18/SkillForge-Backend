package com.skillforge.backend.service.impl;

import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.QuizAttemptDTO;
import com.skillforge.backend.dto.QuizDTO;
import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.EmployeeCourses;
import com.skillforge.backend.entity.Quiz;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.CourseRepository;
import com.skillforge.backend.repository.EmployeeCourseRepository;
import com.skillforge.backend.repository.QuizRepository;
import com.skillforge.backend.service.QuizService;
import com.skillforge.backend.utils.ObjectMappers;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizServiceIMPL implements QuizService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private EmployeeCourseRepository employeeCourseRepository;

    @Override
    public GenericDTO createQuiz(String courseId, List<QuizDTO> quizDTOs) {
        try {
            Course course = courseRepository.findByCourseid(courseId);
            if(course==null) {
                throw new ResourceNotFoundException();
            }
            List<Quiz> quizList = new ArrayList<>();
            for(QuizDTO quizDTO: quizDTOs) {
                Quiz quiz = ObjectMappers.quizDTOtoQuiz(quizDTO);
                quiz.setCourse(course);
                quizList.add(quiz);
            }
            quizRepository.saveAll(quizList);
            return GenericDTO.builder()
                    .message("Quiz added Successfully")
                    .build();
        }catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    @Transactional
    public GenericDTO deleteQuestion(String questionId) {
        try {
            Quiz quiz = quizRepository.findById(questionId);
            if(quiz == null) {
                throw new ResourceNotFoundException();
            }
            quizRepository.delete(quiz);
            return GenericDTO.builder()
                    .message("Question successfully deleted")
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public List<QuizDTO> getCourseQuiz(String courseId) {
        try {
            List<Quiz> quizList = quizRepository.findByCourseCourseid(courseId);
            if(quizList == null) {
                throw new ResourceNotFoundException();
            }
            List<QuizDTO> quizDTOS = new ArrayList<>();
            for(Quiz quiz: quizList) {
                quizDTOS.add(ObjectMappers.quiztoQuizDTO(quiz));
            }
            return quizDTOS;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public List<QuizDTO> getCourseQuizForEmployee(String courseId, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            EmployeeCourses courses = employeeCourseRepository.findByUserIdAndCourseId(user.getUserId(),courseId);
            if(courses==null) {
                throw new ResourceNotFoundException();
            }
            List<Quiz> quizList = quizRepository.findByCourseCourseid(courseId);
            List<QuizDTO> quizDTOS = new ArrayList<>();
            for(Quiz quiz : quizList) {
                quizDTOS.add(ObjectMappers.quiztoQuizDTO(quiz));
            }
            return quizDTOS;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO attemptQuiz(String courseId, Principal connectedUser, List<QuizAttemptDTO> quizAttemptDTOS) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            EmployeeCourses courses = employeeCourseRepository.findByUserIdAndCourseId(user.getUserId(),courseId);
            if(courses==null) {
                throw new ResourceNotFoundException();
            }
            List<Quiz> quizList = quizRepository.findByCourseCourseid(courseId);
            int totalQuestions =  quizList.size();
            int correctMarked = 0;
            for(Quiz quiz : quizList) {
                String id = quiz.getId();
                String correctAns = quiz.getCorrectans();
                Optional<QuizAttemptDTO> quizAttemptDTO = quizAttemptDTOS.stream().filter(dto -> dto.getId().equals(id)).findFirst();
                if(quizAttemptDTO.isPresent() && quizAttemptDTO.get().getAttemptedAns().equals(correctAns)) {
                    correctMarked+=1;
                }
            }
            Double percentage = (double) ((correctMarked/totalQuestions) * 100);
            if(percentage>=75.0) {
                return GenericDTO.builder()
                        .message("Congratulations you have passed the exam with "+percentage+" %!!!")
                        .build();
            } else {
                return GenericDTO.builder()
                        .message("You did not pass the exam please try again.")
                        .build();
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }


}
