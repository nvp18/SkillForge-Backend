package com.skillforge.backend.unittests;

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
import com.skillforge.backend.service.impl.QuizServiceIMPL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class QuizServiceIMPLTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private EmployeeCourseRepository employeeCourseRepository;

    @Mock
    private CourseQuizRepository courseQuizRepository;

    @InjectMocks
    private QuizServiceIMPL quizService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateQuiz_Success() {
        Course course = new Course();
        when(courseRepository.findByCourseid("course1")).thenReturn(course);
        when(courseQuizRepository.save(any(CourseQuiz.class))).thenReturn(new CourseQuiz());

        CourseQuizDTO courseQuizDTO = new CourseQuizDTO();
        QuizDTO quiz =new QuizDTO();
        quiz.setId("1234");
        courseQuizDTO.setQuestions(List.of(quiz));

        GenericDTO result = quizService.createQuiz("course1", courseQuizDTO);

        assertNotNull(result);
        assertEquals("Quiz added Successfully", result.getMessage());
    }

    @Test
    void testCreateQuiz_ResourceNotFoundException() {
        when(courseRepository.findByCourseid("course1")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> quizService.createQuiz("course1", new CourseQuizDTO()));
    }

    @Test
    void testCreateQuiz_InternalServerException() {
        Course course = new Course();
        when(courseRepository.findByCourseid("course1")).thenReturn(course);
        when(courseQuizRepository.save(any(CourseQuiz.class))).thenThrow(new RuntimeException());

        CourseQuizDTO courseQuizDTO = new CourseQuizDTO();
        QuizDTO quiz =new QuizDTO();
        quiz.setId("1234");
        courseQuizDTO.setQuestions(List.of(quiz));

        assertThrows(InternalServerException.class, () -> quizService.createQuiz("course1", courseQuizDTO));
    }

    @Test
    void testDeleteQuestion_Success() {
        Quiz quiz = new Quiz();
        when(quizRepository.findById("question1")).thenReturn(quiz);

        GenericDTO result = quizService.deleteQuestion("question1");

        assertNotNull(result);
        assertEquals("Question successfully deleted", result.getMessage());
        verify(quizRepository, times(1)).delete(quiz);
    }

    @Test
    void testDeleteQuestion_ResourceNotFoundException() {
        when(quizRepository.findById("question1")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> quizService.deleteQuestion("question1"));
    }

    @Test
    void testDeleteQuestion_InternalServerException() {
        Quiz quiz = new Quiz();
        when(quizRepository.findById("question1")).thenThrow(new RuntimeException());

        assertThrows(InternalServerException.class, () -> quizService.deleteQuestion("question1"));
    }

    @Test
    void testGetCourseQuiz_Success() {
        CourseQuiz courseQuiz = new CourseQuiz();
        courseQuiz.setCourse(new Course());
        when(courseQuizRepository.findByCourseCourseid("course1")).thenReturn(courseQuiz);

        CourseQuizDTO result = quizService.getCourseQuiz("course1");

        assertNotNull(result);
    }

    @Test
    void testGetCourseQuiz_ResourceNotFoundException() {
        when(courseQuizRepository.findByCourseCourseid("course1")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> quizService.getCourseQuiz("course1"));
    }

    @Test
    void testGetCourseQuiz_InternalServerException() {
        when(courseQuizRepository.findByCourseCourseid("course1")).thenThrow(new RuntimeException());
        assertThrows(InternalServerException.class, () -> quizService.getCourseQuiz("course1"));
    }

    @Test
    void testAttemptQuiz_Success() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        EmployeeCourses employeeCourses = new EmployeeCourses();
        employeeCourses.setQuizcompleted(false);

        CourseQuiz courseQuiz = new CourseQuiz();
        Quiz quiz = new Quiz();
        quiz.setId("123");
        quiz.setQuestion("question1");
        quiz.setOption1("option1");
        quiz.setOption2("option2");
        quiz.setOption3("option3");
        quiz.setOption4("option4");
        quiz.setCorrectans("option1");
        QuizAttemptDTO quizAttemptDTO = new QuizAttemptDTO();
        quizAttemptDTO.setId("123");
        quizAttemptDTO.setAttemptedAns("option1");
        List<QuizAttemptDTO> quizAttemptDTOS = new ArrayList<>();
        quizAttemptDTOS.add(quizAttemptDTO);
        List<Quiz> quizList = new ArrayList<>();
        quizList.add(quiz);
        courseQuiz.setQuizzes(quizList);

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(employeeCourses);
        when(courseQuizRepository.findByCourseCourseid("course1")).thenReturn(courseQuiz);

        GenericDTO result = quizService.attemptQuiz("course1", principal, quizAttemptDTOS);

        assertNotNull(result);
        verify(employeeCourseRepository, times(1)).save(employeeCourses);
    }

    @Test
    void testAttemptQuiz_Success2() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        EmployeeCourses employeeCourses = new EmployeeCourses();
        employeeCourses.setQuizcompleted(false);

        CourseQuiz courseQuiz = new CourseQuiz();
        Quiz quiz1 = new Quiz();
        quiz1.setId("123");
        quiz1.setQuestion("question1");
        quiz1.setOption1("option1");
        quiz1.setOption2("option2");
        quiz1.setOption3("option3");
        quiz1.setOption4("option4");
        quiz1.setCorrectans("option1");
        Quiz quiz2 = new Quiz();
        quiz2.setId("1234");
        quiz2.setQuestion("question1");
        quiz2.setOption1("option1");
        quiz2.setOption2("option2");
        quiz2.setOption3("option3");
        quiz2.setOption4("option4");
        quiz2.setCorrectans("option1");
        QuizAttemptDTO quizAttemptDTO = new QuizAttemptDTO();
        quizAttemptDTO.setId("123");
        quizAttemptDTO.setAttemptedAns("option1");
        QuizAttemptDTO quizAttemptDTO1 = new QuizAttemptDTO();
        quizAttemptDTO1.setId("1234");
        quizAttemptDTO1.setAttemptedAns("option21");
        List<QuizAttemptDTO> quizAttemptDTOS = new ArrayList<>();
        quizAttemptDTOS.add(quizAttemptDTO);
        quizAttemptDTOS.add(quizAttemptDTO1);
        List<Quiz> quizList = new ArrayList<>();
        quizList.add(quiz1);
        quizList.add(quiz2);
        courseQuiz.setQuizzes(quizList);

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(employeeCourses);
        when(courseQuizRepository.findByCourseCourseid("course1")).thenReturn(courseQuiz);

        GenericDTO result = quizService.attemptQuiz("course1", principal, quizAttemptDTOS);

        assertNotNull(result);
        verify(employeeCourseRepository, times(0)).save(employeeCourses);
    }

    @Test
    void testAttemptQuiz_ResourceNotFoundException() {
        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(null);
        Principal principal = new UsernamePasswordAuthenticationToken(new User(), null);
        assertThrows(ResourceNotFoundException.class, () -> quizService.attemptQuiz("course1", principal, new ArrayList<>()));
    }

    @Test
    void testAttemptQuiz_InternalServerException() {
        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenThrow(new RuntimeException());
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);
        assertThrows(InternalServerException.class, () -> quizService.attemptQuiz("course1", principal, new ArrayList<>()));
    }

    @Test
    void testDeleteQuiz_Success() {
        CourseQuiz courseQuiz = new CourseQuiz();
        when(courseQuizRepository.findById("quiz1")).thenReturn(courseQuiz);

        GenericDTO result = quizService.deleteQuiz("quiz1");

        assertNotNull(result);
        assertEquals("Quiz Delete Successfully", result.getMessage());
    }

    @Test
    void testDeleteQuiz_InternalServerException() {
        when(courseQuizRepository.findById("quiz1")).thenThrow(new InternalServerException());
        assertThrows(InternalServerException.class, () -> quizService.deleteQuiz("quiz1"));
    }

    @Test
    void testGetCourseQuizForEmployee_Success() {
        // Mock user and principal
        User user = new User();
        user.setUserId("user1");

        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        // Mock EmployeeCourses
        EmployeeCourses employeeCourses = new EmployeeCourses();
        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(employeeCourses);

        // Mock CourseQuiz
        CourseQuiz courseQuiz = new CourseQuiz();
        courseQuiz.setId("quiz1");
        courseQuiz.setDescription("Test Description");
        courseQuiz.setTitle("Test Title");
        Course course = new Course();
        course.setCourseid("course1");
        course.setCourseTags("tags");
        course.setCourseDescription("desc");
        course.setDays(12);
        course.setCourseName("testcourse");
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        courseQuiz.setCourse(course);

        List<Quiz> quizzes = new ArrayList<>();
        Quiz quiz = new Quiz();
        quiz.setId("quiz1");
        quizzes.add(quiz);
        courseQuiz.setQuizzes(quizzes);

        when(courseQuizRepository.findByCourseCourseid("course1")).thenReturn(courseQuiz);

        // Call the method
        CourseQuizDTO result = quizService.getCourseQuizForEmployee("course1", principal);

        // Assertions
        assertNotNull(result);
        assertEquals("course1", result.getCourseid());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(1, result.getQuestions().size());
    }

    @Test
    void testGetCourseQuizForEmployee_ResourceNotFoundException() {
        // Mock user and principal
        User user = new User();
        user.setUserId("user1");

        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        // Mock EmployeeCourses not found
        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(null);

        // Expect ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> quizService.getCourseQuizForEmployee("course1", principal));
    }

    @Test
    void testGetCourseQuizForEmployee_InternalServerException() {
        // Mock user and principal
        User user = new User();
        user.setUserId("user1");

        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        // Mock EmployeeCourses
        EmployeeCourses employeeCourses = new EmployeeCourses();
        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(employeeCourses);

        // Mock exception when querying CourseQuizRepository
        when(courseQuizRepository.findByCourseCourseid("course1")).thenThrow(RuntimeException.class);

        // Expect InternalServerException
        assertThrows(InternalServerException.class, () -> quizService.getCourseQuizForEmployee("course1", principal));
    }

    @Test
    void testGetQuizQuestions_Success() {
        // Mock list of quizzes
        List<Quiz> quizzes = new ArrayList<>();
        Quiz quiz = new Quiz();
        quiz.setQuestion("Sample Question?");
        quiz.setOption1("Option 1");
        quiz.setOption2("Option 2");
        quiz.setOption3("Option 3");
        quiz.setOption4("Option 4");
        quiz.setId("quiz1");
        quiz.setCorrectans("Option 1");
        quizzes.add(quiz);

        when(quizRepository.findByCourseQuizId("quiz1")).thenReturn(quizzes);

        // Call the method
        List<QuizDTO> result = quizService.getQuizQuestions("quiz1");

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sample Question?", result.get(0).getQuestion());
        assertEquals("Option 1", result.get(0).getOption1());
        assertEquals("Option 2", result.get(0).getOption2());
        assertEquals("Option 3", result.get(0).getOption3());
        assertEquals("Option 4", result.get(0).getOption4());
        assertEquals("Option 1", result.get(0).getCorrectans());
    }

    @Test
    void testGetQuizQuestions_InternalServerException() {
        // Mock repository to throw an exception
        when(quizRepository.findByCourseQuizId("quiz1")).thenThrow(RuntimeException.class);

        // Expect InternalServerException
        assertThrows(InternalServerException.class, () -> quizService.getQuizQuestions("quiz1"));
    }
}
