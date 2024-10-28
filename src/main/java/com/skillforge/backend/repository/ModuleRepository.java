package com.skillforge.backend.repository;

import com.skillforge.backend.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ModuleRepository  extends JpaRepository<Module, UUID> {

    List<Module> findByCourseCourseid(String courseId);

    Module findByModuleid(String moduleId);

}
