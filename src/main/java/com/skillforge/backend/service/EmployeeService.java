package com.skillforge.backend.service;

import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.EmployeeCourseProgressDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ProgressDTO;
import com.skillforge.backend.entity.EmployeeCourseProgress;

import java.security.Principal;
import java.util.List;

public interface EmployeeService {

    List<EmployeeCourseDTO> getAllCourses(Principal connectedUser);

    GenericDTO updateCompletedModules(String moduleId, String courseId,Principal connectedUser);

    GenericDTO startCourse(String courseId, Principal connectedUser);

    ProgressDTO getCourseProgress(String employeeId, String courseId, Principal connectedUser);

    List<EmployeeCourseProgressDTO> getModuleProgress(String courseId, Principal connectedUser);

    EmployeeCourseDTO getCourseStatus(String courseId, Principal connectedUser);

}
