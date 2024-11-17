package com.skillforge.backend.controllers;

import com.skillforge.backend.dto.*;
import com.skillforge.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ConcernsService concernsService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private QuizService quizService;

    @GetMapping("/getAllEmployeeCourses")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<EmployeeCourseDTO>> getAllEmployeeCourses(Principal connectedUser) {
        List<EmployeeCourseDTO> employeeCourseDTOs = employeeService.getAllCourses(connectedUser);
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

    @GetMapping("/getAllAnnouncements/{courseId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<AnnouncementDTO>> getCourseAnnouncements(@PathVariable("courseId") String courseId) {
        List<AnnouncementDTO> announcementDTOS = announcementService.getCourseAnnouncements(courseId);
        return ResponseEntity.ok().body(announcementDTOS);
    }

    @GetMapping("/getAnnouncement/{announcementId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<AnnouncementDTO> getAnnouncement(@PathVariable("announcementId") String announcementId) {
        AnnouncementDTO announcementDTO = announcementService.getAnnouncement(announcementId);
        return ResponseEntity.ok().body(announcementDTO);
    }

    @PostMapping("/updateModuleCompleted/{moduleId}/{courseId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<GenericDTO> updateCompletedModule(@PathVariable("moduleId") String moduleId,@PathVariable("courseId") String courseId, Principal connectedUser) {
        GenericDTO genericDTO = employeeService.updateCompletedModules(moduleId,courseId ,connectedUser);
        return ResponseEntity.ok().body(genericDTO);
    }

    @GetMapping("/getQuiz/{courseId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<CourseQuizDTO> detCourseQuiz(@PathVariable("courseId") String courseId, Principal connectedUser) {
        CourseQuizDTO quizDTOS = quizService.getCourseQuizForEmployee(courseId,connectedUser);
        return ResponseEntity.ok().body(quizDTOS);
    }

    @PostMapping("/startCourse/{courseId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<GenericDTO> changeCourseStatus(@PathVariable("courseId") String courseId, Principal connectedUser) {
        GenericDTO genericDTO = employeeService.startCourse(courseId,connectedUser);
        return ResponseEntity.ok().body(genericDTO);
    }

    @GetMapping("/viewProgress/{employeeId}/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<ProgressDTO> getProgress(@PathVariable("employeeId") String employeeId, @PathVariable("courseId") String courseId
            , Principal connectedUser) {
        ProgressDTO progressDTO = employeeService.getCourseProgress(employeeId,courseId,connectedUser);
        return ResponseEntity.ok().body(progressDTO);
    }

    @GetMapping("/getModuleStatus/{courseId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<EmployeeCourseProgressDTO>> getModulesProgress(@PathVariable("courseId") String courseId, Principal connectedUser) {
        List<EmployeeCourseProgressDTO> employeeCourseProgressDTOS = employeeService.getModuleProgress(courseId,connectedUser);
        return ResponseEntity.ok().body(employeeCourseProgressDTOS);
    }

    @GetMapping("/getCourseStatus/{courseId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<EmployeeCourseDTO> getCourseStatus(@PathVariable("courseId") String courseId, Principal connectedUser) {
        EmployeeCourseDTO employeeCourseDTO = employeeService.getCourseStatus(courseId,connectedUser);
        return ResponseEntity.ok().body(employeeCourseDTO);
    }
}
