package com.skillforge.backend.service;

import com.skillforge.backend.dto.CourseDTO;

public interface CourseService {

    CourseDTO createCourse(CourseDTO courseDTO);
    CourseDTO updateCourse(CourseDTO courseDTO);
    CourseDTO deleteCourse(CourseDTO courseDTO);
}
