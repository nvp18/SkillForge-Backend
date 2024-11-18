package com.skillforge.backend.unittests;

import com.skillforge.backend.controllers.EmployeeController;
import com.skillforge.backend.dto.*;
import com.skillforge.backend.service.AnnouncementService;
import com.skillforge.backend.service.ConcernsService;
import com.skillforge.backend.service.EmployeeService;
import com.skillforge.backend.service.QuizService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ConcernsService concernsService;

    @Mock
    private AnnouncementService announcementService;

    @Mock
    private QuizService quizService;

    @InjectMocks
    private EmployeeController employeeController;

    public EmployeeControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployeeCourses() {
        Principal mockPrincipal = mock(Principal.class);
        List<EmployeeCourseDTO> mockCourses = Arrays.asList(new EmployeeCourseDTO(), new EmployeeCourseDTO());
        when(employeeService.getAllCourses(mockPrincipal)).thenReturn(mockCourses);

        ResponseEntity<List<EmployeeCourseDTO>> response = employeeController.getAllEmployeeCourses(mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCourses, response.getBody());
        verify(employeeService, times(1)).getAllCourses(mockPrincipal);
    }

    @Test
    void testGetAllConcerns() {
        Principal mockPrincipal = mock(Principal.class);
        List<ConcernDTO> mockConcerns = Arrays.asList(new ConcernDTO(), new ConcernDTO());
        when(concernsService.getEmployeeConcerns(mockPrincipal)).thenReturn(mockConcerns);

        ResponseEntity<List<ConcernDTO>> response = employeeController.getAllConcerns(mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockConcerns, response.getBody());
        verify(concernsService, times(1)).getEmployeeConcerns(mockPrincipal);
    }

    @Test
    void testPostConcern() {
        ConcernDTO concernDTO = new ConcernDTO();
        Principal mockPrincipal = mock(Principal.class);
        GenericDTO mockResponse = new GenericDTO();
        when(concernsService.raiseAConcern(concernDTO, mockPrincipal)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = employeeController.postConcern(concernDTO, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(concernsService, times(1)).raiseAConcern(concernDTO, mockPrincipal);
    }

    @Test
    void testReplyToConcern() {
        String concernId = "1";
        ReplyDTO replyDTO = new ReplyDTO();
        Principal mockPrincipal = mock(Principal.class);
        GenericDTO mockResponse = new GenericDTO();
        when(concernsService.replyToConcern(replyDTO, concernId, mockPrincipal)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = employeeController.replyToConcern(concernId, replyDTO, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(concernsService, times(1)).replyToConcern(replyDTO, concernId, mockPrincipal);
    }

    @Test
    void testGetCourseAnnouncements() {
        String courseId = "101";
        List<AnnouncementDTO> mockAnnouncements = Arrays.asList(new AnnouncementDTO(), new AnnouncementDTO());
        when(announcementService.getCourseAnnouncements(courseId)).thenReturn(mockAnnouncements);

        ResponseEntity<List<AnnouncementDTO>> response = employeeController.getCourseAnnouncements(courseId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAnnouncements, response.getBody());
        verify(announcementService, times(1)).getCourseAnnouncements(courseId);
    }

    @Test
    void testGetAnnouncement() {
        String announcementId = "1";
        AnnouncementDTO mockAnnouncement = new AnnouncementDTO();
        when(announcementService.getAnnouncement(announcementId)).thenReturn(mockAnnouncement);

        ResponseEntity<AnnouncementDTO> response = employeeController.getAnnouncement(announcementId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAnnouncement, response.getBody());
        verify(announcementService, times(1)).getAnnouncement(announcementId);
    }

    @Test
    void testUpdateCompletedModule() {
        String moduleId = "1";
        String courseId = "101";
        Principal mockPrincipal = mock(Principal.class);
        GenericDTO mockResponse = new GenericDTO();
        when(employeeService.updateCompletedModules(moduleId, courseId, mockPrincipal)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = employeeController.updateCompletedModule(moduleId, courseId, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(employeeService, times(1)).updateCompletedModules(moduleId, courseId, mockPrincipal);
    }

    @Test
    void testDetCourseQuiz() {
        String courseId = "101";
        Principal mockPrincipal = mock(Principal.class);
        CourseQuizDTO mockQuiz = new CourseQuizDTO();
        when(quizService.getCourseQuizForEmployee(courseId, mockPrincipal)).thenReturn(mockQuiz);

        ResponseEntity<CourseQuizDTO> response = employeeController.detCourseQuiz(courseId, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockQuiz, response.getBody());
        verify(quizService, times(1)).getCourseQuizForEmployee(courseId, mockPrincipal);
    }

    @Test
    void testChangeCourseStatus() {
        String courseId = "101";
        Principal mockPrincipal = mock(Principal.class);
        GenericDTO mockResponse = new GenericDTO();
        when(employeeService.startCourse(courseId, mockPrincipal)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = employeeController.changeCourseStatus(courseId, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(employeeService, times(1)).startCourse(courseId, mockPrincipal);
    }

    @Test
    void testGetProgress() {
        String employeeId = "1";
        String courseId = "101";
        Principal mockPrincipal = mock(Principal.class);
        ProgressDTO mockProgress = new ProgressDTO();
        when(employeeService.getCourseProgress(employeeId, courseId, mockPrincipal)).thenReturn(mockProgress);

        ResponseEntity<ProgressDTO> response = employeeController.getProgress(employeeId, courseId, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockProgress, response.getBody());
        verify(employeeService, times(1)).getCourseProgress(employeeId, courseId, mockPrincipal);
    }

    @Test
    void testGetModulesProgress() {
        String courseId = "101";
        Principal mockPrincipal = mock(Principal.class);
        List<EmployeeCourseProgressDTO> mockProgress = Arrays.asList(new EmployeeCourseProgressDTO(), new EmployeeCourseProgressDTO());
        when(employeeService.getModuleProgress(courseId, mockPrincipal)).thenReturn(mockProgress);

        ResponseEntity<List<EmployeeCourseProgressDTO>> response = employeeController.getModulesProgress(courseId, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockProgress, response.getBody());
        verify(employeeService, times(1)).getModuleProgress(courseId, mockPrincipal);
    }

    @Test
    void testGetCourseStatus() {
        String courseId = "101";
        Principal mockPrincipal = mock(Principal.class);
        EmployeeCourseDTO mockCourse = new EmployeeCourseDTO();
        when(employeeService.getCourseStatus(courseId, mockPrincipal)).thenReturn(mockCourse);

        ResponseEntity<EmployeeCourseDTO> response = employeeController.getCourseStatus(courseId, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCourse, response.getBody());
        verify(employeeService, times(1)).getCourseStatus(courseId, mockPrincipal);
    }
}
