package com.skillforge.backend.service;

import com.skillforge.backend.dto.CourseDTO;
import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.GenericDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public interface CourseService {

    CourseDTO createCourse(CourseDTO courseDTO);
    CourseDTO updateCourse(CourseDTO courseDTO);
    CourseDTO deleteCourse(CourseDTO courseDTO);

    GenericDTO uploadCourseFiles(String courseName, MultipartFile[] files);

    Map<String, Object> getCourseFiles(String courseId, String fileName) ;

    GenericDTO deleteCourseFile(String courseId,String fileName);

    CourseDTO getCourseDetails(String courseId);

    EmployeeCourseDTO assignCourseToEmployee(String courseId, String employeeID, LocalDateTime dueDate);
}
