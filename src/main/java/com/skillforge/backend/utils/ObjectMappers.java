package com.skillforge.backend.utils;

import com.skillforge.backend.dto.CourseDTO;
import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.UserDTO;
import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.EmployeeCourses;
import com.skillforge.backend.entity.User;

import java.time.LocalDateTime;

public class ObjectMappers {

    public static Course courseDtoToCourseMapper(CourseDTO courseDTO) {
        Course course = new Course();
        course.setCourseName(courseDTO.getCourseName());
        course.setCourseTags(courseDTO.getCourseTags());
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        course.setCourseDirectory(courseDTO.getCourseName());
        course.setCourseDescription(courseDTO.getCourseDescription());
        return course;
    }

    public static CourseDTO courseToCourseDTOMapper(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseId(course.getCourseid());
        courseDTO.setCourseName(course.getCourseName());
        courseDTO.setCourseTags(course.getCourseTags());
        courseDTO.setCreatedAt(course.getCreatedAt().toString());
        courseDTO.setUpdatedAt(course.getUpdatedAt().toString());
        courseDTO.setCourseDirectory(course.getCourseName());
        courseDTO.setCourseDescription(course.getCourseDescription());
        return courseDTO;
    }

    public static EmployeeCourseDTO employeecourseToEmployeecourseDTOMapper(EmployeeCourses course) {
        EmployeeCourseDTO employeeCourseDTO = new EmployeeCourseDTO();
        employeeCourseDTO.setId(course.getId());
        employeeCourseDTO.setCourse(courseToCourseDTOMapper(course.getCourse()));
        employeeCourseDTO.setStatus(course.getStatus());
        employeeCourseDTO.setDueDate(course.getDueDate().toString());
        employeeCourseDTO.setAssignedAt(course.getAssignedAt().toString());
        employeeCourseDTO.setUser(userToUserDTO(course.getUser()));
        return employeeCourseDTO;
    }

    public static UserDTO userToUserDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .userName(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}
