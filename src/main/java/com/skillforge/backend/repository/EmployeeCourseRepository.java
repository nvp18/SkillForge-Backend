package com.skillforge.backend.repository;

import com.skillforge.backend.entity.EmployeeCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeCourseRepository extends JpaRepository<EmployeeCourses, UUID> {

    List<EmployeeCourses> findByUserUserId(String employeeId);

}
