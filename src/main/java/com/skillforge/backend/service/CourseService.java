package com.skillforge.backend.service;

import com.skillforge.backend.dto.CourseDTO;
import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ModuleDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CourseService {

    CourseDTO createCourse(CourseDTO courseDTO);

    List<CourseDTO> getAllCourses();

    CourseDTO updateCourse(CourseDTO courseDTO);
    CourseDTO deleteCourse(CourseDTO courseDTO);

    GenericDTO uploadCourseModules(String courseId, MultipartFile file, String moduleName, int modulenumber);

    List<ModuleDTO> getCourseModules(String courseId);

    Map<String,Object> getModuleContent(String moduleId);

    GenericDTO deleteCourseModule(String moduleId);

    GenericDTO updateCourseModule(String moduleId, MultipartFile file, String moduleName);

    CourseDTO getCourseDetails(String courseId);

    EmployeeCourseDTO assignCourseToEmployee(String courseId, String employeeID, LocalDateTime dueDate);
}
