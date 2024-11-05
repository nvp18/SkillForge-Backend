package com.skillforge.backend.service.impl;

import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.entity.EmployeeCourses;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.EmployeeCourseRepository;
import com.skillforge.backend.service.EmployeeService;
import com.skillforge.backend.utils.ObjectMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceIMPL implements EmployeeService {

    @Autowired
    EmployeeCourseRepository employeeCourseRepository;

    @Override
    public List<EmployeeCourseDTO> getAllCourses(Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            List<EmployeeCourses> employeeCourses = employeeCourseRepository.findByUserUserId(user.getUserId());
            if(employeeCourses == null) {
                throw new ResourceNotFoundException();
            }
            List<EmployeeCourseDTO> employeeCourseDTOS = new ArrayList<>();
            for(EmployeeCourses course : employeeCourses) {
                employeeCourseDTOS.add(ObjectMappers.employeecourseToEmployeecourseDTOMapper(course));
            }
            return employeeCourseDTOS;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO updateCompletedModules(String courseId, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            String userId = user.getUserId();
            EmployeeCourses employeeCourses = employeeCourseRepository.findByUserIdAndCourseId(userId,courseId);
            if(employeeCourses==null) {
                throw new ResourceNotFoundException();
            }
            employeeCourses.setModulesCompleted(employeeCourses.getModulesCompleted()+1);
            employeeCourseRepository.save(employeeCourses);
            return GenericDTO.builder()
                    .message("Updated Module Completed Successfully")
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }
}
