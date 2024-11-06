package com.skillforge.backend.repository;

import com.skillforge.backend.entity.EmployeeCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeCourseRepository extends JpaRepository<EmployeeCourses, UUID> {

    List<EmployeeCourses> findByUserUserId(String employeeId);

    @Query(value = """
            SELECT e FROM EmployeeCourses e where e.user.userId = :userId AND e.course.courseid = :courseId
            """)
    EmployeeCourses findByUserIdAndCourseId(String userId, String courseId);

    @Modifying
    @Query(value = """
            DELETE FROM EmployeeCourses e WHERE e.user.userId = :employeeId AND e.course.courseid = :courseId
            """)
    void deleteByEmployeeIdAndCourseId(String employeeId, String courseId);

    EmployeeCourses findById(String id);

}
