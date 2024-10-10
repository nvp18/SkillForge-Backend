package com.skillforge.backend.service.impl;

import com.skillforge.backend.config.AmazonS3Config;
import com.skillforge.backend.dto.CourseDTO;
import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.EmployeeCourses;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.CourseRepository;
import com.skillforge.backend.repository.EmployeeCourseRepository;
import com.skillforge.backend.repository.UserRepository;
import com.skillforge.backend.service.CourseService;
import com.skillforge.backend.utils.CourseStatus;
import com.skillforge.backend.utils.ObjectMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CourseServiceIMPL  implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    AmazonS3Config s3Config;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmployeeCourseRepository employeeCourseRepository;

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        try {
            Course course = ObjectMappers.courseDtoToCourseMapper(courseDTO);
            Course savedCourse = courseRepository.save(course);
            return  ObjectMappers.courseToCourseDTOMapper(savedCourse);
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public CourseDTO updateCourse(CourseDTO courseDTO) {
        return null;
    }

    @Override
    public CourseDTO deleteCourse(CourseDTO courseDTO) {
        return null;
    }

    @Override
    public GenericDTO uploadCourseFiles(String courseName, MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                if(!s3Config.uploadFiles(courseName, file)) {
                    throw new InternalServerException();
                };
            }
            GenericDTO genericDTO = new GenericDTO();
            genericDTO.setMessage("Files uploaded successfully");
            return genericDTO;
        }catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public Map<String, Object> getCourseFiles(String courseId, String fileName) {
        try {
            Map<String, Object> courseContent = s3Config.getFile(courseId, fileName);
            return courseContent;
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO deleteCourseFile(String courseId, String fileName) {
        try {
            if(s3Config.deleteFile(courseId,fileName)) {
                GenericDTO genericDTO = new GenericDTO();
                genericDTO.setMessage("File "+fileName+" deleted successfully");
                return genericDTO;
            } else {
                throw new InternalServerException();
            }
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public CourseDTO getCourseDetails(String courseId) {
        try {
            Course course = courseRepository.findByCourseid(courseId);
            if(course==null) {
                throw new ResourceNotFoundException();
            }
            CourseDTO courseDTO = ObjectMappers.courseToCourseDTOMapper(course);
            return courseDTO;
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public EmployeeCourseDTO assignCourseToEmployee(String courseId, String employeeID, LocalDateTime dueDate) {
        try {
            User user = userRepository.findByUserId(employeeID);
            if(user == null) {
                throw new ResourceNotFoundException();
            }
            Course course = courseRepository.findByCourseid(courseId);
            if(course == null) {
                throw new ResourceNotFoundException();
            }
            EmployeeCourses employeeCourses = new EmployeeCourses();
            employeeCourses.setCourse(course);
            employeeCourses.setUser(user);
            employeeCourses.setAssignedAt(LocalDateTime.now());
            employeeCourses.setStatus(CourseStatus.NOT_STARTED.toString());
            employeeCourses.setDueDate(dueDate);
            EmployeeCourses savedCourse = employeeCourseRepository.save(employeeCourses);
            return ObjectMappers.employeecourseToEmployeecourseDTOMapper(savedCourse);
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }
}
