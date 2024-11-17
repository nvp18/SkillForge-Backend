package com.skillforge.backend.unittests;

import com.skillforge.backend.config.AmazonS3Config;
import com.skillforge.backend.dto.CourseDTO;
import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.entity.Module;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.entity.*;
import com.skillforge.backend.repository.*;
import com.skillforge.backend.service.impl.CourseServiceIMPL;
import com.skillforge.backend.utils.CourseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CourseServiceIMPLTests {

    @InjectMocks
    private CourseServiceIMPL courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private AmazonS3Config s3Config;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeCourseRepository employeeCourseRepository;

    @Mock
    private EmployeeCourseProgressRepository courseProgressRepository;

    private Course mockCourse;
    private Module mockModule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockCourse = new Course();
        mockCourse.setCourseid("C01");
        mockCourse.setCourseDescription("Sample Course");
        mockCourse.setCourseDirectory("courseDir/");
        mockCourse.setCourseTags("courseTags/");
        mockCourse.setCourseName("Sample Course");
        mockCourse.setCreatedAt(LocalDateTime.now());
        mockCourse.setUpdatedAt(LocalDateTime.now());
        mockCourse.setDays(2);
        mockModule = new Module();
        mockModule.setCourse(mockCourse);
        mockModule.setModulecontent("Sample C");
        mockModule.setModuleName("sample 3");
        mockModule.setModulenumber(34);
        mockModule.setCreatedAt(LocalDateTime.now());
        mockModule.setUpdatedAt(LocalDateTime.now());
        mockModule.setModuleid("Sdadf");
        mockModule.setEmployeeCourseProgressList(new ArrayList<>());
        List<Module> modules = new ArrayList<>();
        modules.add(mockModule);
        mockCourse.setCourseModules(modules);
    }

    @Test
    void testCreateCourseSuccess() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseDescription("Sample Course");
        courseDTO.setCourseId("1234");
        courseDTO.setCourseName("Sample Course");
        courseDTO.setCourseTags("courseTags/");
        courseDTO.setDaysToFinish(2);
        courseDTO.setUpdatedAt("today");
        when(courseRepository.save(any(Course.class))).thenReturn(mockCourse);

        CourseDTO result = courseService.createCourse(courseDTO);

        assertNotNull(result);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testGetAllCoursesSuccess() {
        List<Course> courses = new ArrayList<>();
        courses.add(mockCourse);
        when(courseRepository.findAll()).thenReturn(courses);

        List<CourseDTO> result = courseService.getAllCourses();

        assertEquals(1, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCourseSuccess() {
        CourseDTO courseDTO = new CourseDTO();
        when(courseRepository.findByCourseid("course123")).thenReturn(mockCourse);

        GenericDTO result = courseService.updateCourse(courseDTO, "course123");

        assertNotNull(result);
        assertEquals("Updated Course Successfully", result.getMessage());
    }

    @Test
    void testDeleteCourseSuccess() {
        when(courseRepository.findByCourseid("course123")).thenReturn(mockCourse);
        when(s3Config.deleteCourseModules(anyString())).thenReturn(true);

        GenericDTO result = courseService.deleteCourse("course123");

        assertEquals("Delete Course Successfully", result.getMessage());
        verify(courseRepository, times(1)).delete(mockCourse);
    }

    @Test
    void testUploadCourseModulesSuccess() throws IOException {
        when(courseRepository.findByCourseid("course123")).thenReturn(mockCourse);
        when(s3Config.uploadFiles(anyString(), anyString(), any(MultipartFile.class))).thenReturn("key");

        GenericDTO result = courseService.uploadCourseModules("course123", mock(MultipartFile.class), "moduleName", 1);

        assertEquals("Module uploaded successfully", result.getMessage());
    }

    @Test
    void testGetCourseDetailsSuccess() {
        when(courseRepository.findByCourseid("course123")).thenReturn(mockCourse);

        CourseDTO result = courseService.getCourseDetails("course123");

        assertNotNull(result);
        verify(courseRepository, times(1)).findByCourseid("course123");
    }

    @Test
    void testAssignCourseToEmployeeSuccess() {
        User mockUser = new User();
        when(userRepository.findByUserId("emp123")).thenReturn(mockUser);
        when(courseRepository.findByCourseid("course123")).thenReturn(mockCourse);
        EmployeeCourses employeeCourses = new EmployeeCourses();
        employeeCourses.setCourse(mockCourse);
        employeeCourses.setAssignedAt(LocalDateTime.now());
        employeeCourses.setQuizcompleted(Boolean.TRUE);
        employeeCourses.setUser(mockUser);
        employeeCourses.setStatus(CourseStatus.NOT_STARTED.toString());
        employeeCourses.setDueDate(LocalDateTime.now());
        employeeCourses.setId("emp");
        when(employeeCourseRepository.save(any(EmployeeCourses.class))).thenReturn(employeeCourses);

        EmployeeCourseDTO result;
        result = courseService.assignCourseToEmployee("course123", "emp123");

        assertNotNull(result);
        verify(employeeCourseRepository, times(1)).save(any(EmployeeCourses.class));
    }

    // Add tests for remaining methods like `getModuleContent`, `deleteCourseModule`, `updateCourseModule`, etc.
}
