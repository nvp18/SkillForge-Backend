package com.skillforge.backend.unittests;

import com.skillforge.backend.config.AmazonS3Config;
import com.skillforge.backend.dto.CourseDTO;
import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.ModuleDTO;
import com.skillforge.backend.entity.Module;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.entity.*;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.*;
import com.skillforge.backend.service.impl.CourseServiceIMPL;
import com.skillforge.backend.utils.CourseStatus;
import org.checkerframework.checker.units.qual.C;
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
    void testCreateCourseFailure() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseDescription("Sample Course");
        courseDTO.setCourseId("1234");
        courseDTO.setCourseName("Sample Course");
        courseDTO.setCourseTags("courseTags/");
        courseDTO.setDaysToFinish(2);
        courseDTO.setUpdatedAt("today");
        when(courseRepository.save(any(Course.class))).thenThrow(new RuntimeException());

        assertThrows(InternalServerException.class, () -> courseService.createCourse(courseDTO));

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
    void testGetAllCoursesFailure() {
        List<Course> courses = new ArrayList<>();
        courses.add(mockCourse);
        when(courseRepository.findAll()).thenThrow(new RuntimeException());

        assertThrows(InternalServerException.class, () -> courseService.getAllCourses());

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
    void testUpdateCourseFailure1() {
        CourseDTO courseDTO = new CourseDTO();
        when(courseRepository.findByCourseid("course123")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> courseService.updateCourse(courseDTO,"course123"));


        verify(courseRepository, times(1)).findByCourseid("course123");
    }

    @Test
    void testUpdateCourseFailure2() {
        CourseDTO courseDTO = new CourseDTO();
        when(courseRepository.findByCourseid("course123")).thenThrow(new InternalServerException());

        assertThrows(InternalServerException.class, () -> courseService.updateCourse(courseDTO,"course123"));


        verify(courseRepository, times(1)).findByCourseid("course123");
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
    void testDeleteCourseFailure1() {
        when(courseRepository.findByCourseid("course123")).thenReturn(null);
        when(s3Config.deleteCourseModules(anyString())).thenReturn(true);

        assertThrows(ResourceNotFoundException.class, () -> courseService.deleteCourse("course123"));

        verify(courseRepository, times(1)).findByCourseid("course123");
    }

    @Test
    void testDeleteCourseFailure2() {
        when(courseRepository.findByCourseid("course123")).thenReturn(mockCourse);
        when(s3Config.deleteCourseModules(anyString())).thenReturn(false);

        assertThrows(InternalServerException.class, () -> courseService.deleteCourse("course123"));

        verify(courseRepository, times(1)).findByCourseid("course123");
    }

    @Test
    void testUploadCourseModulesSuccess() throws IOException {
        when(courseRepository.findByCourseid("course123")).thenReturn(mockCourse);
        when(s3Config.uploadFiles(anyString(), anyString(), any(MultipartFile.class))).thenReturn("key");

        GenericDTO result = courseService.uploadCourseModules("course123", mock(MultipartFile.class), "moduleName", 1);

        assertEquals("Module uploaded successfully", result.getMessage());
    }

    @Test
    void testUploadCourseModulesFailure1() throws IOException {
        when(courseRepository.findByCourseid("course123")).thenReturn(null);
        when(s3Config.uploadFiles(anyString(), anyString(), any(MultipartFile.class))).thenReturn("key");

        assertThrows(ResourceNotFoundException.class, () -> courseService.uploadCourseModules("course123",mock(MultipartFile.class)
                , "moduleName", 1));

        verify(courseRepository, times(1)).findByCourseid("course123");
    }

    @Test
    void testUploadCourseModulesFailure2() throws IOException {
        when(courseRepository.findByCourseid("course123")).thenReturn(mockCourse);
        when(s3Config.uploadFiles(anyString(), anyString(), any(MultipartFile.class))).thenThrow(new RuntimeException());

        assertThrows(InternalServerException.class, () -> courseService.uploadCourseModules("course123",mock(MultipartFile.class)
                , "moduleName", 1));

        verify(courseRepository, times(1)).findByCourseid("course123");
    }

    @Test
    void testGetCourseDetailsSuccess() {
        when(courseRepository.findByCourseid("course123")).thenReturn(mockCourse);

        CourseDTO result = courseService.getCourseDetails("course123");

        assertNotNull(result);
        verify(courseRepository, times(1)).findByCourseid("course123");
    }

    @Test
    void testGetCourseDetailsFailure1() {
        when(courseRepository.findByCourseid("course123")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseDetails("course123"));

        verify(courseRepository, times(1)).findByCourseid("course123");
    }

    @Test
    void testGetCourseDetailsFailure2() {
        when(courseRepository.findByCourseid("course123")).thenThrow(new RuntimeException());

        assertThrows(InternalServerException.class, () -> courseService.getCourseDetails("course123"));

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

    @Test
    void testAssignCourseToEmployeeFailure1() {
        User mockUser = new User();
        when(userRepository.findByUserId("emp123")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> courseService.assignCourseToEmployee("course123", "emp123"));
        verify(userRepository, times(1)).findByUserId("emp123");
    }

    @Test
    void testAssignCourseToEmployeeFailure2() {
        User mockUser = new User();
        when(userRepository.findByUserId("emp123")).thenReturn(mockUser);
        when(courseRepository.findByCourseid("course123")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> courseService.assignCourseToEmployee("course123", "emp123"));
        verify(userRepository, times(1)).findByUserId("emp123");
        verify(courseRepository, times(1)).findByCourseid("course123");
    }

    @Test
    void testAssignCourseToEmployeeFailure3() {
        User mockUser = new User();
        when(userRepository.findByUserId("emp123")).thenReturn(mockUser);
        when(courseRepository.findByCourseid("course123")).thenThrow(new RuntimeException());
        assertThrows(InternalServerException.class, () -> courseService.assignCourseToEmployee("course123", "emp123"));
        verify(userRepository, times(1)).findByUserId("emp123");
        verify(courseRepository, times(1)).findByCourseid("course123");
    }

    @Test
    void testGetCourseModules_Success() {
        // Arrange
        String courseId = "course123";
        List<Module> modules = new ArrayList<>();
        Module module1 = new Module();
        module1.setModuleid("module1");
        module1.setModulenumber(123);
        module1.setUpdatedAt(LocalDateTime.now());
        module1.setCreatedAt(LocalDateTime.now());
        Module module2 = new Module();
        module2.setModuleid("module2");
        module2.setUpdatedAt(LocalDateTime.now());
        module2.setCreatedAt(LocalDateTime.now());
        module2.setModulenumber(123);
        modules.add(module1);
        modules.add(module2);

        when(moduleRepository.findByCourseCourseid(courseId)).thenReturn(modules);

        // Act
        List<ModuleDTO> result = courseService.getCourseModules(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("module1", result.get(0).getModuleId());
        assertEquals("module2", result.get(1).getModuleId());

        verify(moduleRepository, times(1)).findByCourseCourseid(courseId);
    }

    @Test
    void testGetCourseModules_EmptyList() {
        // Arrange
        String courseId = "course123";
        when(moduleRepository.findByCourseCourseid(courseId)).thenReturn(new ArrayList<>());

        // Act
        List<ModuleDTO> result = courseService.getCourseModules(courseId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(moduleRepository, times(1)).findByCourseCourseid(courseId);
    }

    @Test
    void testGetCourseModules_InternalServerException() {
        // Arrange
        String courseId = "course123";
        when(moduleRepository.findByCourseCourseid(courseId)).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(InternalServerException.class, () -> courseService.getCourseModules(courseId));
        verify(moduleRepository, times(1)).findByCourseCourseid(courseId);
    }

    @Test
    void testDeassignCourseToEmployee_Success() {
        // Arrange
        String courseId = "course123";
        String employeeId = "employee123";

        // Act
        GenericDTO result = courseService.deassignCourseToEmployee(courseId, employeeId);

        // Assert
        assertNotNull(result);
        assertEquals("Course Deassigned Succesfully", result.getMessage());
        verify(employeeCourseRepository, times(1)).deleteByEmployeeIdAndCourseId(employeeId, courseId);
    }

    @Test
    void testDeassignCourseToEmployee_InternalServerException() {
        // Arrange
        String courseId = "course123";
        String employeeId = "employee123";
        doThrow(new RuntimeException()).when(employeeCourseRepository).deleteByEmployeeIdAndCourseId(employeeId, courseId);

        // Act & Assert
        assertThrows(InternalServerException.class, () -> courseService.deassignCourseToEmployee(courseId, employeeId));
        verify(employeeCourseRepository, times(1)).deleteByEmployeeIdAndCourseId(employeeId, courseId);
    }

    @Test
    void testGetAllCoursesOfEmployee_Success() {
        // Arrange
        String employeeId = "employee123";
        List<EmployeeCourses> employeeCourses = new ArrayList<>();
        EmployeeCourses course1 = new EmployeeCourses();
        EmployeeCourses course2 = new EmployeeCourses();
        course1.setDueDate(LocalDateTime.now());
        course1.setAssignedAt(LocalDateTime.now());
        course2.setDueDate(LocalDateTime.now());
        course2.setAssignedAt(LocalDateTime.now());
        Course course = new Course();
        Course course3 = new Course();
        course.setCourseid("course1");
        course.setUpdatedAt(LocalDateTime.now());
        course.setCreatedAt(LocalDateTime.now());
        course1.setCourse(course);
        course3.setUpdatedAt(LocalDateTime.now());
        course3.setCreatedAt(LocalDateTime.now());
        course3.setCourseid("course2");
        course2.setCourse(course3);
        employeeCourses.add(course1);
        employeeCourses.add(course2);

        when(employeeCourseRepository.findByUserUserId(employeeId)).thenReturn(employeeCourses);

        // Act
        List<EmployeeCourseDTO> result = courseService.getAllCoursesOfEmployee(employeeId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("course1", result.get(0).getCourse().getCourseId());
        assertEquals("course2", result.get(1).getCourse().getCourseId());
        verify(employeeCourseRepository, times(1)).findByUserUserId(employeeId);
    }

    @Test
    void testGetAllCoursesOfEmployee_InternalServerException() {
        // Arrange
        String employeeId = "employee123";
        when(employeeCourseRepository.findByUserUserId(employeeId)).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(InternalServerException.class, () -> courseService.getAllCoursesOfEmployee(employeeId));
        verify(employeeCourseRepository, times(1)).findByUserUserId(employeeId);
    }

    @Test
    void testGetModuleContent_Success() throws IOException {
        String moduleId = "module123";
        Module module = new Module();
        module.setModulecontent("path/to/file");
        when(moduleRepository.findByModuleid(moduleId)).thenReturn(module);
        when(s3Config.getPreSignedURL("path/to/file")).thenReturn("https://example.com/pre-signed-url");

        GenericDTO result = courseService.getModuleContent(moduleId);

        assertNotNull(result);
        assertEquals("https://example.com/pre-signed-url", result.getMessage());
        verify(moduleRepository, times(1)).findByModuleid(moduleId);
        verify(s3Config, times(1)).getPreSignedURL("path/to/file");
    }

    @Test
    void testGetModuleContent_ResourceNotFoundException() {
        String moduleId = "module123";
        when(moduleRepository.findByModuleid(moduleId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> courseService.getModuleContent(moduleId));
        verify(moduleRepository, times(1)).findByModuleid(moduleId);
    }

    @Test
    void testGetModuleContent_InternalServerException() {
        String moduleId = "module123";
        when(moduleRepository.findByModuleid(moduleId)).thenThrow(new RuntimeException());

        assertThrows(InternalServerException.class, () -> courseService.getModuleContent(moduleId));
        verify(moduleRepository, times(1)).findByModuleid(moduleId);
    }

    // Test for deleteCourseModule
    @Test
    void testDeleteCourseModule_Success() {
        String moduleId = "module123";
        Module module = new Module();
        module.setModulecontent("path/to/file");

        when(moduleRepository.findByModuleid(moduleId)).thenReturn(module);
        when(s3Config.deleteFile("path/to/file")).thenReturn(true);

        GenericDTO result = courseService.deleteCourseModule(moduleId);

        assertNotNull(result);
        assertEquals("Module deleted successfully", result.getMessage());
        verify(moduleRepository, times(1)).findByModuleid(moduleId);
        verify(moduleRepository, times(1)).delete(module);
        verify(s3Config, times(1)).deleteFile("path/to/file");
    }

    @Test
    void testDeleteCourseModule_ResourceNotFoundException() {
        String moduleId = "module123";
        when(moduleRepository.findByModuleid(moduleId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> courseService.deleteCourseModule(moduleId));
        verify(moduleRepository, times(1)).findByModuleid(moduleId);
    }

    @Test
    void testDeleteCourseModule_InternalServerException() {
        String moduleId = "module123";
        Module module = new Module();
        module.setModulecontent("path/to/file");

        when(moduleRepository.findByModuleid(moduleId)).thenReturn(module);
        when(s3Config.deleteFile("path/to/file")).thenReturn(false);

        assertThrows(InternalServerException.class, () -> courseService.deleteCourseModule(moduleId));
        verify(moduleRepository, times(1)).findByModuleid(moduleId);
    }

    // Test for updateCourseModule
    @Test
    void testUpdateCourseModule_Success() throws IOException {
        String moduleId = "module123";
        String moduleName = "Updated Module";
        MultipartFile file = mock(MultipartFile.class);
        Module module = new Module();
        module.setModulecontent("course123/moduleName/oldFile");

        when(moduleRepository.findByModuleid(moduleId)).thenReturn(module);
        when(s3Config.deleteFile("course123/moduleName/oldFile")).thenReturn(true);
        when(s3Config.uploadFiles("course123", moduleName, file)).thenReturn("course123/moduleName/newFile");
        when(moduleRepository.save(any(Module.class))).thenReturn(module);

        GenericDTO result = courseService.updateCourseModule(moduleId, file, moduleName);

        assertNotNull(result);
        assertEquals("Module Updated Successfully", result.getMessage());
        verify(moduleRepository, times(1)).findByModuleid(moduleId);
        verify(s3Config, times(1)).deleteFile("course123/moduleName/oldFile");
        verify(s3Config, times(1)).uploadFiles("course123", moduleName, file);
        verify(moduleRepository, times(1)).save(any(Module.class));
    }

    @Test
    void testUpdateCourseModule_ResourceNotFoundException() {
        String moduleId = "module123";
        MultipartFile file = mock(MultipartFile.class);

        when(moduleRepository.findByModuleid(moduleId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> courseService.updateCourseModule(moduleId, file, "Updated Module"));
        verify(moduleRepository, times(1)).findByModuleid(moduleId);
    }

    @Test
    void testUpdateCourseModule_InternalServerException() {
        String moduleId = "module123";
        String moduleName = "Updated Module";
        MultipartFile file = mock(MultipartFile.class);
        Module module = new Module();
        module.setModulecontent("course123/moduleName/oldFile");

        when(moduleRepository.findByModuleid(moduleId)).thenReturn(module);
        when(s3Config.deleteFile("course123/moduleName/oldFile")).thenReturn(false);

        assertThrows(InternalServerException.class, () -> courseService.updateCourseModule(moduleId, file, moduleName));
        verify(moduleRepository, times(1)).findByModuleid(moduleId);
    }

    // Add tests for remaining methods like `getModuleContent`, `deleteCourseModule`, `updateCourseModule`, etc.
}
