package com.skillforge.backend.controllers;

import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/getAllEmployeeCourses/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<EmployeeCourseDTO>> getAllEmployeeCourses(@PathVariable("employeeId") String employeeId) {
        List<EmployeeCourseDTO> employeeCourseDTOs = employeeService.getAllCourses(employeeId);
        return ResponseEntity.ok().body(employeeCourseDTOs);
    }




}
