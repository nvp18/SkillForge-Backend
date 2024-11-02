package com.skillforge.backend.controllers;

import com.skillforge.backend.dto.*;
import com.skillforge.backend.service.AnnouncementService;
import com.skillforge.backend.service.ConcernsService;
import com.skillforge.backend.service.EmployeeService;
//import com.skillforge.backend.service.QuizService;
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
    private EmployeeService employeeService;

    @Autowired
    private ConcernsService concernsService;

    @Autowired
    private AnnouncementService announcementService;

    //@Autowired
    //private QuizService quizService;

    @GetMapping("/getAllEmployeeCourses/{employeeId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
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

    @GetMapping("/getAllAnnouncements/{courseId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<AnnouncementDTO>> getCourseAnnouncements(@PathVariable("courseId") String courseId) {
        List<AnnouncementDTO> announcementDTOS = announcementService.getCourseAnnouncements(courseId);
        return ResponseEntity.ok().body(announcementDTOS);
    }

    /*@GetMapping("/getAnnouncement/{announcementId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<AnnouncementDTO> getAnnouncement(@PathVariable("announcementId") String announcementId) {
        AnnouncementDTO announcementDTO = announcementService.getAnnouncement(announcementId);
        return ResponseEntity.ok().body(announcementDTO);
    }

    @PostMapping("/updateModuleCompleted/{courseId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<GenericDTO> updateCompletedModule(@PathVariable("courseId") String courseId, Principal connectedUser) {
        GenericDTO genericDTO = employeeService.updateCompletedModules(courseId,connectedUser);
        return ResponseEntity.ok().body(genericDTO);
    }

    @GetMapping("/getQuiz/{courseId}")
    @PreAuthorize("hasAutority('EMPLOYEE')")
    public ResponseEntity<List<QuizDTO>> detCourseQuiz(@PathVariable("courseId") String courseId) {
        List<QuizDTO> quizDTOS = quizService.getCourseQuiz(courseId);
        return ResponseEntity.ok().body(quizDTOS);
    }*/


}
