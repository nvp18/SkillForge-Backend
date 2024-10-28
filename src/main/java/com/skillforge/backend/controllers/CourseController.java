package com.skillforge.backend.controllers;

import com.skillforge.backend.dto.CourseDTO;
import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ModuleDTO;
import com.skillforge.backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
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

    @GetMapping("/getAllCourses")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courseDTOS = courseService.getAllCourses();
        return ResponseEntity.ok().body(courseDTOS);
    }

    @PostMapping("/uploadCourseModule/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> uploadCourseModule(@PathVariable("courseId") String courseId, @RequestParam("file") MultipartFile file
            , @RequestParam("modulename") String moduleName, @RequestParam("modulenumber") int modulenumber)  {
        GenericDTO genericDTO = courseService.uploadCourseModules(courseId,file,moduleName,modulenumber);
        return ResponseEntity.ok().body(genericDTO);
    }

    @GetMapping("/getCourseModules/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<ModuleDTO>> getCourseModules(@PathVariable("courseId") String courseId) {
        List<ModuleDTO> moduleDTOS = courseService.getCourseModules(courseId);
        return ResponseEntity.ok().body(moduleDTOS);
    }

    @GetMapping("/getModuleContent/{moduleId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<InputStreamResource> getModuleContent(@PathVariable("moduleId") String moduleId) {
        Map<String, Object> map = courseService.getModuleContent(moduleId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\""+map.get("modulecontent"+"\""))
                .contentType(MediaType.APPLICATION_PDF)
                .body((InputStreamResource) map.get("inputstream"));
    }

    @DeleteMapping("/deleteCourseModule/{moduleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> deleteCourseModule(@PathVariable("moduleId") String moduleId) {
        GenericDTO genericDTO = courseService.deleteCourseModule(moduleId);
        return ResponseEntity.ok().body(genericDTO);
    }

    @PutMapping("/updateCourseModule/{moduleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> updateCourseModule(@PathVariable("moduleId") String moduleId,@RequestParam("file") MultipartFile file
            , @RequestParam("modulename") String moduleName) {
        GenericDTO genericDTO = courseService.updateCourseModule(moduleId,file,moduleName);
        return ResponseEntity.ok().body(genericDTO);
    }

    @GetMapping("/getCourseDetails/{courseID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CourseDTO> getCourseDetails(@PathVariable("courseID") String courseId) {
        CourseDTO courseDTO = courseService.getCourseDetails(courseId);
        return ResponseEntity.ok().body(courseDTO);
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
