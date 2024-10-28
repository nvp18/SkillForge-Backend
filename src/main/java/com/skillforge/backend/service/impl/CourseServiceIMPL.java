package com.skillforge.backend.service.impl;

import com.skillforge.backend.config.AmazonS3Config;
import com.skillforge.backend.dto.CourseDTO;
import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ModuleDTO;
import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.EmployeeCourses;
import com.skillforge.backend.entity.Module;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.CourseRepository;
import com.skillforge.backend.repository.EmployeeCourseRepository;
import com.skillforge.backend.repository.ModuleRepository;
import com.skillforge.backend.repository.UserRepository;
import com.skillforge.backend.service.CourseService;
import com.skillforge.backend.utils.CourseStatus;
import com.skillforge.backend.utils.ObjectMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceIMPL  implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AmazonS3Config s3Config;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeCourseRepository employeeCourseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

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
    public List<CourseDTO> getAllCourses() {
        try {
            List<Course> courses = courseRepository.findAll();
            List<CourseDTO> courseDTOS = new ArrayList<>();
            for(Course course : courses) {
                courseDTOS.add(ObjectMappers.courseToCourseDTOMapper(course));
            }
            return courseDTOS;
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
    public GenericDTO uploadCourseModules(String courseId, MultipartFile file, String moduleName, int modulenumber) {
        try {
            Course course = courseRepository.findByCourseid(courseId);
            if(course==null) {
                throw new ResourceNotFoundException();
            }
            String courseDirectory = course.getCourseDirectory();
            String key = s3Config.uploadFiles(courseDirectory,moduleName,file);
            Module module = new Module();
            module.setModuleName(moduleName);
            module.setCourse(course);
            module.setCreatedAt(LocalDateTime.now());
            module.setUpdatedAt(LocalDateTime.now());
            module.setModulecontent(key);
            module.setModulenumber(modulenumber);
            moduleRepository.save(module);
            GenericDTO genericDTO = new GenericDTO();
            genericDTO.setMessage("Module uploaded successfully");
            return genericDTO;
        }catch (Exception e) {
            if(e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException();
            }
            throw new InternalServerException();
        }
    }

    @Override
    public List<ModuleDTO> getCourseModules(String courseId) {
        try {
            List<Module> modules = moduleRepository.findByCourseCourseid(courseId);
            List<ModuleDTO> moduleDTOS = new ArrayList<>();
            for(Module module: modules) {
                moduleDTOS.add(ObjectMappers.moduleToModuleDTO(module));
            }
            return moduleDTOS;
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public Map<String,Object> getModuleContent(String moduleId) {
        try {
            Module module = moduleRepository.findByModuleid(moduleId);
            if(module == null) {
                throw new ResourceNotFoundException();
            }
            String modulecontent = module.getModulecontent();
            InputStreamResource inputStreamResource = s3Config.getFile(modulecontent);
            Map<String, Object> map = new HashMap<>();
            map.put("key",modulecontent);
            map.put("inputstream",inputStreamResource);
            return map;
        } catch (Exception e) {
            if(e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException();
            }
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO deleteCourseModule(String moduleId) {
        try {
            Module module = moduleRepository.findByModuleid(moduleId);
            if(module==null) {
                throw new ResourceNotFoundException();
            }
            String key = module.getModulecontent();
            if(s3Config.deleteFile(key)) {
                GenericDTO genericDTO = new GenericDTO();
                genericDTO.setMessage("Module deleted successfully");
                moduleRepository.delete(module);
                return genericDTO;
            } else {
                throw new InternalServerException();
            }
        }catch (Exception e) {
            if(e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException();
            }
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO updateCourseModule(String moduleId, MultipartFile file, String moduleName) {
        try {
          Module module = moduleRepository.findByModuleid(moduleId);
          if(module==null) {
              throw new ResourceNotFoundException();
          }
          String key = module.getModulecontent();
          if(s3Config.deleteFile(key)) {
              String courseDirectory = key.split("/")[0];
              String updatedKey = s3Config.uploadFiles(courseDirectory,moduleName,file);
              module.setModulecontent(updatedKey);
              module.setUpdatedAt(LocalDateTime.now());
              moduleRepository.save(module);
              return GenericDTO.builder()
                      .message("Module Updated Successfully")
                      .build();
          } else {
              throw new InternalServerException();
          }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
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
