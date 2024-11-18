package com.skillforge.backend.unittests;

import com.skillforge.backend.controllers.CourseController;
import com.skillforge.backend.dto.*;
import com.skillforge.backend.service.CourseService;
import com.skillforge.backend.service.DiscussionService;
import com.skillforge.backend.service.QuizService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private QuizService quizService;

    @Mock
    private DiscussionService discussionService;

    @InjectMocks
    private CourseController courseController;

    public CourseControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCourse() {
        CourseDTO courseDTO = new CourseDTO();
        CourseDTO mockSavedCourse = new CourseDTO();

        when(courseService.createCourse(courseDTO)).thenReturn(mockSavedCourse);

        ResponseEntity<CourseDTO> response = courseController.createCourse(courseDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockSavedCourse, response.getBody());
        verify(courseService, times(1)).createCourse(courseDTO);
    }

    @Test
    void testUpdateCourseDetails() {
        CourseDTO courseDTO = new CourseDTO();
        String courseId = "123";
        GenericDTO mockResponse = new GenericDTO();

        when(courseService.updateCourse(courseDTO, courseId)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.updateCourseDetails(courseDTO, courseId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(courseService, times(1)).updateCourse(courseDTO, courseId);
    }

    @Test
    void testDeleteCourse() {
        String courseId = "123";
        GenericDTO mockResponse = new GenericDTO();

        when(courseService.deleteCourse(courseId)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.deleteCourse(courseId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(courseService, times(1)).deleteCourse(courseId);
    }

    @Test
    void testGetAllCourses() {
        List<CourseDTO> mockCourses = Arrays.asList(new CourseDTO(), new CourseDTO());

        when(courseService.getAllCourses()).thenReturn(mockCourses);

        ResponseEntity<List<CourseDTO>> response = courseController.getAllCourses();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCourses, response.getBody());
        verify(courseService, times(1)).getAllCourses();
    }

    @Test
    void testUploadCourseModule() {
        String courseId = "123";
        MultipartFile mockFile = mock(MultipartFile.class);
        String moduleName = "Module 1";
        int moduleNumber = 1;
        GenericDTO mockResponse = new GenericDTO();

        when(courseService.uploadCourseModules(courseId, mockFile, moduleName, moduleNumber)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.uploadCourseModule(courseId, mockFile, moduleName, moduleNumber);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(courseService, times(1)).uploadCourseModules(courseId, mockFile, moduleName, moduleNumber);
    }

    @Test
    void testGetCourseModules() {
        String courseId = "123";
        List<ModuleDTO> mockModules = Arrays.asList(new ModuleDTO(), new ModuleDTO());

        when(courseService.getCourseModules(courseId)).thenReturn(mockModules);

        ResponseEntity<List<ModuleDTO>> response = courseController.getCourseModules(courseId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockModules, response.getBody());
        verify(courseService, times(1)).getCourseModules(courseId);
    }

    @Test
    void testGetModuleContent() {
        String moduleId = "321";
        GenericDTO mockResponse = new GenericDTO();

        when(courseService.getModuleContent(moduleId)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.getModuleContent(moduleId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(courseService, times(1)).getModuleContent(moduleId);
    }

    @Test
    void testDeleteCourseModule() {
        String moduleId = "321";
        GenericDTO mockResponse = new GenericDTO();

        when(courseService.deleteCourseModule(moduleId)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.deleteCourseModule(moduleId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(courseService, times(1)).deleteCourseModule(moduleId);
    }

    @Test
    void testAssignCourseToEmployee() {
        String courseId = "123";
        String employeeId = "456";
        EmployeeCourseDTO mockResponse = new EmployeeCourseDTO();

        when(courseService.assignCourseToEmployee(courseId, employeeId)).thenReturn(mockResponse);

        ResponseEntity<EmployeeCourseDTO> response = courseController.assignCourseToEmployee(courseId, employeeId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(courseService, times(1)).assignCourseToEmployee(courseId, employeeId);
    }

    @Test
    void testCreateQuiz() {
        String courseId = "123";
        CourseQuizDTO courseQuizDTO = new CourseQuizDTO();
        GenericDTO mockResponse = new GenericDTO();

        when(quizService.createQuiz(courseId, courseQuizDTO)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.createQuiz(courseId, courseQuizDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(quizService, times(1)).createQuiz(courseId, courseQuizDTO);
    }

    @Test
    void testPostDiscussion() {
        String courseId = "123";
        DiscussionDTO discussionDTO = new DiscussionDTO();
        Principal mockPrincipal = mock(Principal.class);
        GenericDTO mockResponse = new GenericDTO();

        when(discussionService.postDiscussion(courseId, discussionDTO, mockPrincipal)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.postDiscussion(courseId, discussionDTO, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(discussionService, times(1)).postDiscussion(courseId, discussionDTO, mockPrincipal);
    }

    @Test
    void testReplyToDiscussion() {
        String discussionId = "123";
        DiscussionReplyDTO discussionReplyDTO = new DiscussionReplyDTO();
        Principal mockPrincipal = mock(Principal.class);
        GenericDTO mockResponse = new GenericDTO();

        when(discussionService.replyToDiscussion(discussionReplyDTO, discussionId, mockPrincipal)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.replyToDiscussion(discussionId, discussionReplyDTO, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(discussionService, times(1)).replyToDiscussion(discussionReplyDTO, discussionId, mockPrincipal);
    }

    @Test
    void testGetAllDiscussions() {
        String courseId = "101";
        Principal mockPrincipal = mock(Principal.class);
        List<DiscussionDTO> mockDiscussions = Arrays.asList(new DiscussionDTO(), new DiscussionDTO());

        when(discussionService.getCourseDiscussions(courseId, mockPrincipal)).thenReturn(mockDiscussions);

        ResponseEntity<List<DiscussionDTO>> response = courseController.getAllDiscussions(courseId, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockDiscussions, response.getBody());
        verify(discussionService, times(1)).getCourseDiscussions(courseId, mockPrincipal);
    }

    @Test
    void testDeleteDiscussion() {
        String discussionId = "321";
        GenericDTO mockResponse = new GenericDTO();

        when(discussionService.deleteDiscussion(discussionId)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.deleteDiscussion(discussionId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(discussionService, times(1)).deleteDiscussion(discussionId);
    }

    @Test
    void testGetAllCoursesOfEmployee() {
        String employeeId = "456";
        List<EmployeeCourseDTO> mockCourses = Arrays.asList(new EmployeeCourseDTO(), new EmployeeCourseDTO());

        when(courseService.getAllCoursesOfEmployee(employeeId)).thenReturn(mockCourses);

        ResponseEntity<List<EmployeeCourseDTO>> response = courseController.getAllCoursesOfEmployee(employeeId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCourses, response.getBody());
        verify(courseService, times(1)).getAllCoursesOfEmployee(employeeId);
    }

    @Test
    void testAttemptQuiz() {
        String courseId = "789";
        Principal mockPrincipal = mock(Principal.class);
        List<QuizAttemptDTO> mockQuizAttempts = Arrays.asList(new QuizAttemptDTO(), new QuizAttemptDTO());
        GenericDTO mockResponse = new GenericDTO();

        when(quizService.attemptQuiz(courseId, mockPrincipal, mockQuizAttempts)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.attemptQuiz(courseId, mockPrincipal, mockQuizAttempts);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(quizService, times(1)).attemptQuiz(courseId, mockPrincipal, mockQuizAttempts);
    }

    @Test
    void testDeleteQuiz() {
        String quizId = "quiz123";
        GenericDTO mockResponse = new GenericDTO();

        when(quizService.deleteQuiz(quizId)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.deleteQuiz(quizId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(quizService, times(1)).deleteQuiz(quizId);
    }

    @Test
    void testDeleteQuizQuestion() {
        String questionId = "question123";
        GenericDTO mockResponse = new GenericDTO();

        when(quizService.deleteQuestion(questionId)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.deleteQuizQuestion(questionId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(quizService, times(1)).deleteQuestion(questionId);
    }

    @Test
    void testDetCourseQuiz() {
        String courseId = "course123";
        CourseQuizDTO mockQuizDTO = new CourseQuizDTO();

        when(quizService.getCourseQuiz(courseId)).thenReturn(mockQuizDTO);

        ResponseEntity<CourseQuizDTO> response = courseController.detCourseQuiz(courseId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockQuizDTO, response.getBody());
        verify(quizService, times(1)).getCourseQuiz(courseId);
    }

    @Test
    void testGetQuizQuestions() {
        String quizId = "quiz123";
        List<QuizDTO> mockQuizQuestions = Arrays.asList(new QuizDTO(), new QuizDTO());

        when(quizService.getQuizQuestions(quizId)).thenReturn(mockQuizQuestions);

        ResponseEntity<List<QuizDTO>> response = courseController.getQuizQuestions(quizId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockQuizQuestions, response.getBody());
        verify(quizService, times(1)).getQuizQuestions(quizId);
    }

    @Test
    void testUpdateCourseModule() {
        String moduleId = "module123";
        MultipartFile mockFile = mock(MultipartFile.class);
        String moduleName = "Updated Module";
        GenericDTO mockResponse = new GenericDTO();

        when(courseService.updateCourseModule(moduleId, mockFile, moduleName)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.updateCourseModule(moduleId, mockFile, moduleName);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(courseService, times(1)).updateCourseModule(moduleId, mockFile, moduleName);
    }

    @Test
    void testGetCourseDetails() {
        String courseId = "course123";
        CourseDTO mockCourseDTO = new CourseDTO();

        when(courseService.getCourseDetails(courseId)).thenReturn(mockCourseDTO);

        ResponseEntity<CourseDTO> response = courseController.getCourseDetails(courseId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCourseDTO, response.getBody());
        verify(courseService, times(1)).getCourseDetails(courseId);
    }

    @Test
    void testDeassignCourseToEmployee() {
        String courseId = "course123";
        String employeeId = "employee123";
        GenericDTO mockResponse = new GenericDTO();

        when(courseService.deassignCourseToEmployee(courseId, employeeId)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = courseController.deassignCourseToEmployee(courseId, employeeId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(courseService, times(1)).deassignCourseToEmployee(courseId, employeeId);
    }
}
