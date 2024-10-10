package com.skillforge.backend.service.impl;

import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.entity.EmployeeCourses;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.EmployeeCourseRepository;
import com.skillforge.backend.service.EmployeeService;
import com.skillforge.backend.utils.ObjectMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceIMPL implements EmployeeService {

    @Autowired
    EmployeeCourseRepository employeeCourseRepository;

    @Override
    public List<EmployeeCourseDTO> getAllCourses(String employeeId) {
        try {
            List<EmployeeCourses> employeeCourses = employeeCourseRepository.findByUserUserId(employeeId);
            if(employeeCourses.isEmpty()) {
                throw new ResourceNotFoundException();
            }
            List<EmployeeCourseDTO> employeeCourseDTOS = new ArrayList<>();
            for(EmployeeCourses course : employeeCourses) {
                employeeCourseDTOS.add(ObjectMappers.employeecourseToEmployeecourseDTOMapper(course));
            }
            return employeeCourseDTOS;
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }
}
