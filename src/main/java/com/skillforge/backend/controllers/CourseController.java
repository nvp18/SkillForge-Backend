package com.skillforge.backend.controllers;

import com.skillforge.backend.dto.CourseDTO;
import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/createCourse")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO){
        CourseDTO savedCourse = courseService.createCourse(courseDTO);
        return ResponseEntity.ok().body(savedCourse);
    }

    @PostMapping("/uploadCourseContent/{courseName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> uploadCourseContent(@PathVariable("courseName") String courseName, @RequestParam MultipartFile[] files)  {
        GenericDTO genericDTO = courseService.uploadCourseFiles(courseName,files);
        return ResponseEntity.ok().body(genericDTO);
    }

    @GetMapping("/getCourseDetails/{courseID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CourseDTO> getCourseDetails(@PathVariable("courseID") String courseId) {
        CourseDTO courseDTO = courseService.getCourseDetails(courseId);
        return ResponseEntity.ok().body(courseDTO);
    }

    @GetMapping("/getCourseContent/{courseId}/{fileName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String,Object>> getCourseContent(@PathVariable("courseId") String courseId, @PathVariable("fileName") String fileName) {
        Map<String,Object> courseContent = courseService.getCourseFiles(courseId,fileName);
        return ResponseEntity.ok().body(courseContent);
    }

    @DeleteMapping("/deleteCourseContent/{courseId}/{fileName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> deleteCourseContent(@PathVariable("courseId") String courseId, @PathVariable("fileName") String fileName) {
        GenericDTO genericDTO = courseService.deleteCourseFile(courseId,fileName);
        return ResponseEntity.ok().body(genericDTO);
    }

    @PostMapping("/assignCourseToEmployee/{courseId}/{employeeId}/{dateTime}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EmployeeCourseDTO> assignCourseToEmployee(@PathVariable("courseId") String courseId,
                                                                    @PathVariable("employeeId") String employeeId,
                                                                    @PathVariable("dateTime") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime dateTime) {
        EmployeeCourseDTO employeeCourseDTO = courseService.assignCourseToEmployee(courseId,employeeId,dateTime);
        return ResponseEntity.ok().body(employeeCourseDTO);
    }
}
