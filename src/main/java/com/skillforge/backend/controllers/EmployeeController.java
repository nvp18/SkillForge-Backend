package com.skillforge.backend.controllers;

import com.skillforge.backend.dto.ConcernDTO;
import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ReplyDTO;
import com.skillforge.backend.service.ConcernsService;
import com.skillforge.backend.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ConcernsService concernsService;

    @GetMapping("/getAllEmployeeCourses/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<EmployeeCourseDTO>> getAllEmployeeCourses(@PathVariable("employeeId") String employeeId) {
        List<EmployeeCourseDTO> employeeCourseDTOs = employeeService.getAllCourses(employeeId);
        return ResponseEntity.ok().body(employeeCourseDTOs);
    }

    @GetMapping("/getConcerns")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<ConcernDTO>> getAllConcerns(Principal connectedUser) {
        List<ConcernDTO> concernDTOS = concernsService.getEmployeeConcerns(connectedUser);
        return ResponseEntity.ok().body(concernDTOS);
    }

    @PostMapping("/raiseConcern")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<GenericDTO> postConcern(@RequestBody ConcernDTO concernDTO, Principal connectedUser) {
        GenericDTO genericDTO = concernsService.raiseAConcern(concernDTO,connectedUser);
        return ResponseEntity.ok().body(genericDTO);
    }

    @PostMapping("/replyToConcern/{concernId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<GenericDTO> replyToConcern(@PathVariable("concernId") String concernId, @RequestBody ReplyDTO replyDTO, Principal connectedUser) {
        GenericDTO genericDTO = concernsService.replyToConcern(replyDTO,concernId,connectedUser);
        return ResponseEntity.ok().body(genericDTO);
    }


}
