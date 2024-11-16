package com.skillforge.backend.service;

import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ProgressDTO;

import java.security.Principal;
import java.util.List;

public interface EmployeeService {

    List<EmployeeCourseDTO> getAllCourses(Principal connectedUser);

    GenericDTO updateCompletedModules(String moduleId, String courseId,Principal connectedUser);

    GenericDTO startCourse(String courseId, Principal connectedUser);

    ProgressDTO getCourseProgress(String employeeId, String courseId, Principal connectedUser);

}
