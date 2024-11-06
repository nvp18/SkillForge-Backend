package com.skillforge.backend.repository;

import com.skillforge.backend.entity.EmployeeCourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EmployeeCourseProgressRepository extends JpaRepository<EmployeeCourseProgress, UUID> {

    @Query(value = """
            SELECT e FROM EmployeeCourseProgress e where e.employeeCourses.id = :id AND e.module.moduleid = :moduleid
            """)
    EmployeeCourseProgress findByEmployeeCourseIdAndModuleId(String id, String moduleid);

    @Query(value = """
            SELECT e FROM EmployeeCourseProgress e where e.employeeCourses.id = :id
            """)
    List<EmployeeCourseProgress> findByEmployeeCourseId(String id);
}
