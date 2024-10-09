package com.skillforge.backend.service;

import com.skillforge.backend.dto.EmployeeCourseDTO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeCourseDTO> getAllCourses(String employeeId);

}
