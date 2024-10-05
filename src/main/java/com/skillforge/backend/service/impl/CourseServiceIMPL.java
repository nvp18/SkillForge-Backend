package com.skillforge.backend.service.impl;

import com.skillforge.backend.dto.CourseDTO;
import com.skillforge.backend.entity.Course;
import com.skillforge.backend.repository.CourseRepository;
import com.skillforge.backend.service.CourseService;
import com.skillforge.backend.utils.ObjectMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceIMPL  implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = ObjectMappers.courseDtoToCourseMapper(courseDTO);
        Course savedCourse = courseRepository.save(course);
        courseDTO.setCourseId(savedCourse.getCourseId());
        courseDTO.setCreatedAt(savedCourse.getCreatedAt());
        courseDTO.setUpdatedAt(savedCourse.getUpdatedAt());
        return courseDTO;
    }

    @Override
    public CourseDTO updateCourse(CourseDTO courseDTO) {
        return null;
    }

    @Override
    public CourseDTO deleteCourse(CourseDTO courseDTO) {
        return null;
    }
}
