package com.skillforge.backend.service.impl;

import com.skillforge.backend.dto.CourseQuizDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.QuizAttemptDTO;
import com.skillforge.backend.dto.QuizDTO;
import com.skillforge.backend.entity.*;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.CourseQuizRepository;
import com.skillforge.backend.repository.CourseRepository;
import com.skillforge.backend.repository.EmployeeCourseRepository;
import com.skillforge.backend.repository.QuizRepository;
import com.skillforge.backend.service.QuizService;
import com.skillforge.backend.utils.CourseStatus;
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

    @Autowired
    private CourseQuizRepository courseQuizRepository;

    @Override
    public GenericDTO createQuiz(String courseId, CourseQuizDTO courseQuizDTO) {
        try {
            Course course = courseRepository.findByCourseid(courseId);
            if(course==null) {
                throw new ResourceNotFoundException();
            }
            List<Quiz> quizList = new ArrayList<>();
            CourseQuiz courseQuiz = CourseQuiz.builder()
                    .course(course)
                    .description(courseQuizDTO.getDescription())
                    .title(courseQuizDTO.getTitle())
                    .build();
            CourseQuiz savedQuiz = courseQuizRepository.save(courseQuiz);
            for(QuizDTO quizDTO: courseQuizDTO.getQuestions()) {
                Quiz quiz = ObjectMappers.quizDTOtoQuiz(quizDTO);
                quiz.setCoursequiz(savedQuiz);
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
    public CourseQuizDTO getCourseQuiz(String courseId) {
        try {
            CourseQuiz courseQuiz = courseQuizRepository.findByCourseCourseid(courseId);
            if(courseQuiz == null) {
                throw new ResourceNotFoundException();
            }
            CourseQuizDTO courseQuizDTO =  CourseQuizDTO.builder()
                    .courseid(courseQuiz.getCourse().getCourseid())
                    .id(courseQuiz.getId())
                    .description(courseQuiz.getDescription())
                    .title(courseQuiz.getTitle())
                    .build();
            return courseQuizDTO;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public CourseQuizDTO getCourseQuizForEmployee(String courseId, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            EmployeeCourses courses = employeeCourseRepository.findByUserIdAndCourseId(user.getUserId(),courseId);
            if(courses==null) {
                throw new ResourceNotFoundException();
            }
            CourseQuiz courseQuiz = courseQuizRepository.findByCourseCourseid(courseId);
            CourseQuizDTO courseQuizDTO = CourseQuizDTO.builder()
                    .courseid(courseQuiz.getCourse().getCourseid())
                    .id(courseQuiz.getId())
                    .description(courseQuiz.getDescription())
                    .title(courseQuiz.getTitle())
                    .build();
            List<QuizDTO> quizDTOS = new ArrayList<>();
            for(Quiz quiz: courseQuiz.getQuizzes()) {
                quizDTOS.add(ObjectMappers.quiztoQuizDTO(quiz));
            }
            courseQuizDTO.setQuestions(quizDTOS);
            return courseQuizDTO;
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
            CourseQuiz courseQuiz = courseQuizRepository.findByCourseCourseid(courseId);
            int totalQuestions =  courseQuiz.getQuizzes().size();
            int correctMarked = 0;
            for(Quiz quiz : courseQuiz.getQuizzes()) {
                String id = quiz.getId();
                String correctAns = quiz.getCorrectans();
                Optional<QuizAttemptDTO> quizAttemptDTO = quizAttemptDTOS.stream().filter(dto -> dto.getId().equals(id)).findFirst();
                if(quizAttemptDTO.isPresent() && quizAttemptDTO.get().getAttemptedAns().equals(correctAns)) {
                    correctMarked+=1;
                }
            }
            Double percentage = (double) ((correctMarked/totalQuestions) * 100);
            if(percentage>=75.0) {
                courses.setStatus(CourseStatus.COMPLETED.toString());
                courses.setQuizcompleted(Boolean.TRUE);
                employeeCourseRepository.save(courses);
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

    @Override
    public List<QuizDTO> getQuizQuestions(String quizId) {
        try {
            List<Quiz> quizzes = quizRepository.findByCourseQuizId(quizId);
            List<QuizDTO> quizDTOS = new ArrayList<>();
            for(Quiz quiz: quizzes) {
                QuizDTO quizDTO = QuizDTO.builder()
                        .question(quiz.getQuestion())
                        .option1(quiz.getOption1())
                        .option2(quiz.getOption2())
                        .option4(quiz.getOption4())
                        .option3(quiz.getOption3())
                        .id(quiz.getId())
                        .correctans(quiz.getCorrectans())
                        .build();
                quizDTOS.add(quizDTO);
            }
            return quizDTOS;
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO deleteQuiz(String quizId) {
        try {
            CourseQuiz courseQuiz = courseQuizRepository.findById(quizId);
            courseQuizRepository.delete(courseQuiz);
            return GenericDTO.builder()
                    .message("Quiz Delete Successfully")
                    .build();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

}
