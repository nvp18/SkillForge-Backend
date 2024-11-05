package com.skillforge.backend.service;

import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.GenericDTO;

import java.security.Principal;
import java.util.List;

public interface EmployeeService {

    List<EmployeeCourseDTO> getAllCourses(Principal connectedUser);

    GenericDTO updateCompletedModules(String courseId, Principal connectedUser);

}
