package com.skillforge.backend.unittests;

import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.EmployeeCourseProgressDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ProgressDTO;
import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.EmployeeCourseProgress;
import com.skillforge.backend.entity.EmployeeCourses;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.entity.Module;
import com.skillforge.backend.exception.GenericException;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.EmployeeCourseProgressRepository;
import com.skillforge.backend.repository.EmployeeCourseRepository;
import com.skillforge.backend.service.impl.EmployeeServiceIMPL;
import com.skillforge.backend.utils.CourseStatus;
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
import static org.mockito.Mockito.*;
public class EmployeeServiceIMPLTest {

    @Mock
    private EmployeeCourseRepository employeeCourseRepository;

    @Mock
    private EmployeeCourseProgressRepository courseProgressRepository;

    @InjectMocks
    private EmployeeServiceIMPL employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCourses_Success() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        List<EmployeeCourses> employeeCoursesList = new ArrayList<>();
        EmployeeCourses employeeCourse = new EmployeeCourses();
        Course course = new Course();
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        employeeCourse.setCourse(course);
        employeeCourse.setAssignedAt(LocalDateTime.now());
        employeeCourse.setDueDate(LocalDateTime.now());
        employeeCoursesList.add(employeeCourse);

        when(employeeCourseRepository.findByUserUserId("user1")).thenReturn(employeeCoursesList);

        List<EmployeeCourseDTO> result = employeeService.getAllCourses(principal);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllCourses_ResourceNotFoundException() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        when(employeeCourseRepository.findByUserUserId("user1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getAllCourses(principal));
    }

    @Test
    void testGetAllCourses_InternalServeException() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        when(employeeCourseRepository.findByUserUserId("user1")).thenThrow(new RuntimeException());

        assertThrows(InternalServerException.class, () -> employeeService.getAllCourses(principal));
    }

    @Test
    void testUpdateCompletedModules_Success() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        EmployeeCourses employeeCourses = new EmployeeCourses();
        employeeCourses.setId("course1");
        employeeCourses.setStatus(CourseStatus.STARTED.toString());

        EmployeeCourseProgress courseProgress = new EmployeeCourseProgress();
        courseProgress.setIsCompleted(false);

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(employeeCourses);
        when(courseProgressRepository.findByEmployeeCourseIdAndModuleId("course1", "module1")).thenReturn(courseProgress);

        GenericDTO result = employeeService.updateCompletedModules("module1", "course1", principal);

        assertNotNull(result);
        assertEquals("Updated Module Completed Successfully", result.getMessage());
    }

    @Test
    void testUpdateCompletedModules_ResourceNotFoundException() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        EmployeeCourses employeeCourses = new EmployeeCourses();
        employeeCourses.setId("course1");
        employeeCourses.setStatus(CourseStatus.STARTED.toString());

        EmployeeCourseProgress courseProgress = new EmployeeCourseProgress();
        courseProgress.setIsCompleted(false);

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(employeeCourses);
        when(courseProgressRepository.findByEmployeeCourseIdAndModuleId("course1", "module1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.updateCompletedModules("module1", "course1", principal));
    }

    @Test
    void testUpdateCompletedModules_InternalServerException() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        EmployeeCourses employeeCourses = new EmployeeCourses();
        employeeCourses.setId("course1");
        employeeCourses.setStatus(CourseStatus.STARTED.toString());

        EmployeeCourseProgress courseProgress = new EmployeeCourseProgress();
        courseProgress.setIsCompleted(false);

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(employeeCourses);
        when(courseProgressRepository.findByEmployeeCourseIdAndModuleId("course1", "module1")).thenThrow(new RuntimeException());

        assertThrows(InternalServerException.class, () -> employeeService.updateCompletedModules("module1", "course1", principal));
    }

    @Test
    void testUpdateCompletedModules_GenericException() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        EmployeeCourses employeeCourses = new EmployeeCourses();
        employeeCourses.setStatus(CourseStatus.NOT_STARTED.toString());

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(employeeCourses);

        assertThrows(GenericException.class, () -> employeeService.updateCompletedModules("module1", "course1", principal));
    }

    @Test
    void testStartCourse_Success() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        EmployeeCourses employeeCourses = new EmployeeCourses();

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(employeeCourses);

        GenericDTO result = employeeService.startCourse("course1", principal);

        assertNotNull(result);
        assertEquals("Course status changed to started", result.getMessage());
    }

    @Test
    void testStartCourse_ResourceNotFoundException() {
        User user = new User();
        user.setUserId("user1");
        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(null);
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.startCourse("course1", principal));
    }

    @Test
    void testStartCourse_InternalServerException() {
        User user = new User();
        user.setUserId("user1");
        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenThrow(new RuntimeException());
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        assertThrows(InternalServerException.class, () -> employeeService.startCourse("course1", principal));
    }

    @Test
    void testGetCourseProgress_Success() {
        EmployeeCourses employeeCourses = new EmployeeCourses();
        employeeCourses.setId("course1");

        List<EmployeeCourseProgress> progressList = new ArrayList<>();
        EmployeeCourseProgress progress = new EmployeeCourseProgress();
        progress.setIsCompleted(true);
        progressList.add(progress);

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(employeeCourses);
        when(courseProgressRepository.findByEmployeeCourseId("course1")).thenReturn(progressList);

        ProgressDTO result = employeeService.getCourseProgress("user1", "course1", mock(Principal.class));

        assertNotNull(result);
        assertEquals(100.0, result.getCourseProgress());
    }

    @Test
    void testGetCourseProgress_ResourceNotFoundError() {
        EmployeeCourses employeeCourses = new EmployeeCourses();
        employeeCourses.setId("course1");

        List<EmployeeCourseProgress> progressList = new ArrayList<>();
        EmployeeCourseProgress progress = new EmployeeCourseProgress();
        progress.setIsCompleted(true);
        progressList.add(progress);

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(null);
        when(courseProgressRepository.findByEmployeeCourseId("course1")).thenReturn(progressList);

        assertThrows(ResourceNotFoundException.class,()->employeeService.getCourseProgress("user1", "course1", mock(Principal.class)));

    }

    @Test
    void testGetCourseProgress_InternalServerException() {
        EmployeeCourses employeeCourses = new EmployeeCourses();
        employeeCourses.setId("course1");

        List<EmployeeCourseProgress> progressList = new ArrayList<>();
        EmployeeCourseProgress progress = new EmployeeCourseProgress();
        progress.setIsCompleted(true);
        progressList.add(progress);

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenThrow(new RuntimeException());
        when(courseProgressRepository.findByEmployeeCourseId("course1")).thenReturn(progressList);

        assertThrows(InternalServerException.class,()->employeeService.getCourseProgress("user1", "course1", mock(Principal.class)));

    }

    @Test
    void testGetCourseStatus_Success() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        EmployeeCourses employeeCourses = new EmployeeCourses();
        employeeCourses.setStatus(CourseStatus.COMPLETED.toString());

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(employeeCourses);

        EmployeeCourseDTO result = employeeService.getCourseStatus("course1", principal);

        assertNotNull(result);
        assertEquals(CourseStatus.COMPLETED.toString(), result.getStatus());
    }

    @Test
    void testGetCourseStatus_ResourceNotFoundException() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);
        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getCourseStatus("course1", principal));
    }

    @Test
    void testGetCourseStatus_InternalServerException() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);
        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenThrow(new RuntimeException());

        assertThrows(InternalServerException.class, () -> employeeService.getCourseStatus("course1", principal));
    }

    @Test
    void testGetModuleProgress_Success() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        EmployeeCourses employeeCourses = new EmployeeCourses();
        employeeCourses.setId("course1");

        Module module = new Module();
        module.setModuleid("module1");

        EmployeeCourseProgress progress = new EmployeeCourseProgress();
        progress.setId("progress1");
        progress.setModule(module);
        progress.setIsCompleted(true);

        List<EmployeeCourseProgress> progressList = new ArrayList<>();
        progressList.add(progress);

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(employeeCourses);
        when(courseProgressRepository.findByEmployeeCourseId("course1")).thenReturn(progressList);

        List<EmployeeCourseProgressDTO> result = employeeService.getModuleProgress("course1", principal);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("progress1", result.get(0).getId());
        assertEquals("module1", result.get(0).getModuleId());
        assertTrue(result.get(0).getIsCompleted());
    }

    @Test
    void testGetModuleProgress_ResourceNotFoundException() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getModuleProgress("course1", principal));
    }

    @Test
    void testGetModuleProgress_InternalServerException() {
        User user = new User();
        user.setUserId("user1");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        when(employeeCourseRepository.findByUserIdAndCourseId("user1", "course1")).thenThrow(RuntimeException.class);

        assertThrows(InternalServerException.class, () -> employeeService.getModuleProgress("course1", principal));
    }


}
