package com.skillforge.backend.repository;

import com.skillforge.backend.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ModuleRepository  extends JpaRepository<Module, UUID> {

    List<Module> findByCourseCourseid(String courseId);

    Module findByModuleid(String moduleId);

    @Modifying
    @Query(value = """
      DELETE FROM Module m WHERE m.course.courseid = :courseId
      """)
    void deleteCourseModules(String courseId);

}
