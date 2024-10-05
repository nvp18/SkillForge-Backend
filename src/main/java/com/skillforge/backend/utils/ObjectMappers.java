package com.skillforge.backend.utils;

import com.skillforge.backend.dto.CourseDTO;
import com.skillforge.backend.entity.Course;

import java.time.LocalDateTime;

public class ObjectMappers {

    public static Course courseDtoToCourseMapper(CourseDTO courseDTO) {
        Course course = new Course();
        course.setCourseName(courseDTO.getCourseName());
        course.setCourseTags(courseDTO.getCourseTags());
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        return course;
    }
}
