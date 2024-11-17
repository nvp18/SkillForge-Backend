package com.skillforge.backend.service.impl;

import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.EmployeeCourseProgressDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ProgressDTO;
import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.EmployeeCourseProgress;
import com.skillforge.backend.entity.EmployeeCourses;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.exception.GenericException;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.EmployeeCourseProgressRepository;
import com.skillforge.backend.repository.EmployeeCourseRepository;
import com.skillforge.backend.service.EmployeeService;
import com.skillforge.backend.utils.CourseStatus;
import com.skillforge.backend.utils.ObjectMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceIMPL implements EmployeeService {

    @Autowired
    private EmployeeCourseRepository employeeCourseRepository;

    @Autowired
    private EmployeeCourseProgressRepository courseProgressRepository;

    @Override
    public List<EmployeeCourseDTO> getAllCourses(Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            List<EmployeeCourses> employeeCourses = employeeCourseRepository.findByUserUserId(user.getUserId());
            if(employeeCourses == null) {
                throw new ResourceNotFoundException();
            }
            List<EmployeeCourseDTO> employeeCourseDTOS = new ArrayList<>();
            for(EmployeeCourses course : employeeCourses) {
                employeeCourseDTOS.add(ObjectMappers.employeecourseToEmployeecourseDTOMapper(course));
            }
            return employeeCourseDTOS;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO updateCompletedModules(String moduleId, String courseId, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            EmployeeCourses courses = employeeCourseRepository.findByUserIdAndCourseId(user.getUserId(),courseId);
            if(courses.getStatus().equals(CourseStatus.STARTED.toString())) {
                EmployeeCourseProgress employeeCourseProgress = courseProgressRepository.findByEmployeeCourseIdAndModuleId(courses.getId(), moduleId);
                if (employeeCourseProgress == null) {
                    throw new ResourceNotFoundException();
                }
                employeeCourseProgress.setIsCompleted(Boolean.TRUE);
                courseProgressRepository.save(employeeCourseProgress);
                return GenericDTO.builder()
                        .message("Updated Module Completed Successfully")
                        .build();
            } else {
                throw new GenericException();
            }
        } catch (GenericException e) {
            throw new GenericException();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO startCourse(String courseId, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            String userId = user.getUserId();
            EmployeeCourses employeeCourses = employeeCourseRepository.findByUserIdAndCourseId(userId,courseId);
            if(employeeCourses==null) {
                throw new ResourceNotFoundException();
            }
            employeeCourses.setStatus(CourseStatus.STARTED.toString());
            employeeCourseRepository.save(employeeCourses);
            return GenericDTO.builder()
                    .message("Course status changed to started")
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public ProgressDTO getCourseProgress(String employeeId, String courseId, Principal connectedUser) {
        try {
            EmployeeCourses employeeCourses = employeeCourseRepository.findByUserIdAndCourseId(employeeId, courseId);
            if(employeeCourses==null) {
                throw new ResourceNotFoundException();
            }
            List<EmployeeCourseProgress> employeeCourseProgressList = courseProgressRepository.findByEmployeeCourseId(employeeCourses.getId());
            Long modulesCompleted = employeeCourseProgressList.stream().filter(emp -> emp.getIsCompleted()).count();
            Long totalModules = (long) employeeCourseProgressList.size();
            Double courseProgress = Double.valueOf((modulesCompleted/totalModules) * 100);
            return ProgressDTO.builder()
                    .courseProgress(courseProgress)
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public List<EmployeeCourseProgressDTO> getModuleProgress(String courseId, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            EmployeeCourses employeeCourses = employeeCourseRepository.findByUserIdAndCourseId(user.getUserId(), courseId);
            if(employeeCourses==null) {
                throw new ResourceNotFoundException();
            }
            List<EmployeeCourseProgress> employeeCourseProgressList = courseProgressRepository.findByEmployeeCourseId(employeeCourses.getId());
            List<EmployeeCourseProgressDTO> employeeCourseProgressDTOS = new ArrayList<>();
            for(EmployeeCourseProgress employeeCourseProgress : employeeCourseProgressList) {
                EmployeeCourseProgressDTO employeeCourseProgressDTO = EmployeeCourseProgressDTO.builder()
                        .id(employeeCourseProgress.getId())
                        .moduleId(employeeCourseProgress.getModule().getModuleid())
                        .isCompleted(employeeCourseProgress.getIsCompleted())
                        .build();
                employeeCourseProgressDTOS.add(employeeCourseProgressDTO);
            }
            return employeeCourseProgressDTOS;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public EmployeeCourseDTO getCourseStatus(String courseId, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            EmployeeCourses employeeCourses = employeeCourseRepository.findByUserIdAndCourseId(user.getUserId(), courseId);
            if(employeeCourses==null) {
                throw new ResourceNotFoundException();
            }
            EmployeeCourseDTO employeeCourseDTO = EmployeeCourseDTO.builder()
                    .status(employeeCourses.getStatus())
                    .build();
            return employeeCourseDTO;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }
}
