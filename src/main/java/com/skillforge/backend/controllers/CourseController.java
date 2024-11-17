package com.skillforge.backend.controllers;

import com.skillforge.backend.dto.*;
import com.skillforge.backend.service.CourseService;
import com.skillforge.backend.service.DiscussionService;
import com.skillforge.backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private DiscussionService discussionService;

    @PostMapping("/createCourse")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO){
        CourseDTO savedCourse = courseService.createCourse(courseDTO);
        return ResponseEntity.ok().body(savedCourse);
    }

    @PutMapping("/updateCourse/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> updateCourseDetails(@RequestBody CourseDTO courseDTO,@PathVariable("courseId") String courseId) {
        GenericDTO genericDTO = courseService.updateCourse(courseDTO,courseId);
        return ResponseEntity.ok().body(genericDTO);
    }

    @DeleteMapping("/deleteCourse/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> deleteCourse(@PathVariable("courseId") String courseId) {
        GenericDTO genericDTO = courseService.deleteCourse(courseId);
        return ResponseEntity.ok().body(genericDTO);
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
    public ResponseEntity<GenericDTO> getModuleContent(@PathVariable("moduleId") String moduleId) {
        GenericDTO preSignedURL = courseService.getModuleContent(moduleId);
        return ResponseEntity.ok().body(preSignedURL);
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
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<CourseDTO> getCourseDetails(@PathVariable("courseID") String courseId) {
        CourseDTO courseDTO = courseService.getCourseDetails(courseId);
        return ResponseEntity.ok().body(courseDTO);
    }

    @PostMapping("/assignCourseToEmployee/{courseId}/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EmployeeCourseDTO> assignCourseToEmployee(@PathVariable("courseId") String courseId, @PathVariable("employeeId") String employeeId) {
        EmployeeCourseDTO employeeCourseDTO = courseService.assignCourseToEmployee(courseId,employeeId);
        return ResponseEntity.ok().body(employeeCourseDTO);
    }

    @DeleteMapping("/deassignCourseToEmployee/{courseId}/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> deassignCourseToEmployee(@PathVariable("courseId") String courseId, @PathVariable("employeeId") String employeeId) {
        GenericDTO genericDTO = courseService.deassignCourseToEmployee(courseId,employeeId);
        return ResponseEntity.ok().body(genericDTO);
    }

    @PostMapping("/createQuiz/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> createQuiz(@PathVariable("courseId") String courseId, @RequestBody CourseQuizDTO courseQuizDTO) {
        GenericDTO genericDTO = quizService.createQuiz(courseId,courseQuizDTO);
        return ResponseEntity.ok().body(genericDTO);
    }

    @DeleteMapping("/deleteQuiz/{quizId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> deleteQuiz(@PathVariable("quizId") String quizId) {
        GenericDTO genericDTO = quizService.deleteQuiz(quizId);
        return ResponseEntity.ok().body(genericDTO);
    }

    @DeleteMapping("/deleteQuestion/{questionId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> deleteQuizQuestion(@PathVariable("questionId") String questionId) {
        GenericDTO genericDTO = quizService.deleteQuestion(questionId);
        return ResponseEntity.ok().body(genericDTO);
    }

    @GetMapping("/getQuiz/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<CourseQuizDTO> detCourseQuiz(@PathVariable("courseId") String courseId) {
        CourseQuizDTO quizDTOS = quizService.getCourseQuiz(courseId);
        return ResponseEntity.ok().body(quizDTOS);
    }

    @GetMapping("/getQuestions/{quizId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<QuizDTO>> getQuizQuestions(@PathVariable("quizId") String quizId) {
        List<QuizDTO> quizDTOS = quizService.getQuizQuestions(quizId);
        return ResponseEntity.ok().body(quizDTOS);
    }

    @PostMapping("/postDiscussion/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<GenericDTO> postDiscussion(@PathVariable("courseId") String courseId, @RequestBody DiscussionDTO discussionDTO,
                                                     Principal connectedUser) {
        GenericDTO genericDTO = discussionService.postDiscussion(courseId,discussionDTO,connectedUser);
        return ResponseEntity.ok().body(genericDTO);
    }

    @PostMapping("/replyToDiscussion/{discussionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<GenericDTO> replyToDiscussion(@PathVariable("discussionId") String discussionId,@RequestBody DiscussionReplyDTO discussionReplyDTO,
                                                        Principal connectedUser) {
        GenericDTO genericDTO = discussionService.replyToDiscussion(discussionReplyDTO,discussionId,connectedUser);
        return ResponseEntity.ok().body(genericDTO);
    }

    @GetMapping("/getAllDiscussions/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<DiscussionDTO>> getAllDiscussions(@PathVariable("courseId") String courseId, Principal connectedUser) {
        List<DiscussionDTO> discussionDTOS = discussionService.getCourseDiscussions(courseId,connectedUser);
        return ResponseEntity.ok().body(discussionDTOS);
    }

    @DeleteMapping("/deleteDiscussion/{discussionId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> deleteDiscussion(@PathVariable("discussionId") String discussionId) {
        GenericDTO genericDTO = discussionService.deleteDiscussion(discussionId);
        return ResponseEntity.ok().body(genericDTO);
    }

    @GetMapping("/getAllCoursesOfEmployee/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<EmployeeCourseDTO>> getAllCoursesOfEmployee(@PathVariable("employeeId") String employeeId) {
        List<EmployeeCourseDTO> employeeCourseDTOS = courseService.getAllCoursesOfEmployee(employeeId);
        return ResponseEntity.ok().body(employeeCourseDTOS);
    }

    @PostMapping("/submitQuiz/{courseId}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE','ADMIN')")
    public ResponseEntity<GenericDTO> attemptQuiz(@PathVariable("courseId") String courseId, Principal connectedUser
            ,@RequestBody List<QuizAttemptDTO> quizAttemptDTOS) {
        GenericDTO genericDTO = quizService.attemptQuiz(courseId,connectedUser,quizAttemptDTOS);
        return ResponseEntity.ok().body(genericDTO);
    }
}
