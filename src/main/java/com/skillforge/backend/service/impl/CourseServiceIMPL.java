package com.skillforge.backend.service.impl;

import com.skillforge.backend.config.AmazonS3Config;
import com.skillforge.backend.dto.CourseDTO;
import com.skillforge.backend.dto.EmployeeCourseDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ModuleDTO;
import com.skillforge.backend.entity.*;
import com.skillforge.backend.entity.Module;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.*;
import com.skillforge.backend.service.CourseService;
import com.skillforge.backend.utils.CourseStatus;
import com.skillforge.backend.utils.ObjectMappers;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private EmployeeCourseProgressRepository courseProgressRepository;

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
    public GenericDTO updateCourse(CourseDTO courseDTO,String courseId) {
        try {
            Course course = courseRepository.findByCourseid(courseId);
            if(course == null) {
                throw new ResourceNotFoundException();
            }
            course.setCourseDescription(courseDTO.getCourseDescription());
            course.setCourseTags(courseDTO.getCourseTags());
            course.setDays(courseDTO.getDaysToFinish());
            courseRepository.save(course);
            return GenericDTO.builder()
                    .message("Updated Course Successfully").build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    @Transactional
    public GenericDTO deleteCourse(String courseId) {
        try {
            Course course = courseRepository.findByCourseid(courseId);
            if(course==null) {
                throw new ResourceNotFoundException();
            }
            String courseDirectory = course.getCourseDirectory();
            if(s3Config.deleteCourseModules(courseDirectory)) {
                courseRepository.delete(course);
                GenericDTO genericDTO = GenericDTO.builder()
                        .message("Delete Course Successfully")
                        .build();
                return genericDTO;
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
    public GenericDTO getModuleContent(String moduleId) {
        try {
            Module module = moduleRepository.findByModuleid(moduleId);
            if(module == null) {
                throw new ResourceNotFoundException();
            }
            String modulecontent = module.getModulecontent();
            String preSignedURL = s3Config.getPreSignedURL(modulecontent);
            return GenericDTO.builder()
                    .message(preSignedURL)
                    .build();
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
                moduleRepository.delete(module);
                GenericDTO genericDTO = new GenericDTO();
                genericDTO.setMessage("Module deleted successfully");
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
            if (course == null) {
                throw new ResourceNotFoundException();
            }
            return ObjectMappers.courseToCourseDTOMapper(course);
        } catch (ResourceNotFoundException e) {
            // Let this exception propagate, as it's expected in some cases
            throw e;
        } catch (Exception e) {
            // Catch only unexpected exceptions and rethrow as InternalServerException
            throw new InternalServerException();
        }
    }


    @Override
    public EmployeeCourseDTO assignCourseToEmployee(String courseId, String employeeID) {
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
            employeeCourses.setDueDate(LocalDateTime.now().plusDays(course.getDays()));
            employeeCourses.setQuizcompleted(Boolean.FALSE);
            EmployeeCourses savedCourse = employeeCourseRepository.save(employeeCourses);
            List<EmployeeCourseProgress> employeeCourseProgressList = new ArrayList<>();
            List<Module> modules = course.getCourseModules();
            for(Module module : modules) {
                EmployeeCourseProgress employeeCourseProgress = new EmployeeCourseProgress();
                employeeCourseProgress.setEmployeeCourses(savedCourse);
                employeeCourseProgress.setModule(module);
                employeeCourseProgress.setIsCompleted(Boolean.FALSE);
                employeeCourseProgressList.add(employeeCourseProgress);
            }
            courseProgressRepository.saveAll(employeeCourseProgressList);
            return ObjectMappers.employeecourseToEmployeecourseDTOMapper(savedCourse);
        } catch (ResourceNotFoundException e) {
            throw e;
        }
        // Let this exception propagate, as it's expected in some cas
        catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    @Transactional
    public GenericDTO deassignCourseToEmployee(String courseId, String employeeId) {
        try {
            employeeCourseRepository.deleteByEmployeeIdAndCourseId(employeeId,courseId);
            return GenericDTO.builder()
                    .message("Course Deassigned Succesfully")
                    .build();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public List<EmployeeCourseDTO> getAllCoursesOfEmployee(String employeeId) {
        try {
            List<EmployeeCourses> employeeCourses = employeeCourseRepository.findByUserUserId(employeeId);
            List<EmployeeCourseDTO> employeeCourseDTOS = new ArrayList<>();
            for(EmployeeCourses courses: employeeCourses) {
                EmployeeCourseDTO employeeCourseDTO = ObjectMappers.employeecourseToEmployeecourseDTOMapper(courses);
                employeeCourseDTOS.add(employeeCourseDTO);
            }
            return employeeCourseDTOS;
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }
}
