package com.skillforge.backend.repository;

import com.skillforge.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsernameAndPassword(String userName, String password);
    User findByUsername(String userName);

    @Query("Select role from User where username=?1")
    String findUserRole(String username);

    User findByUserId(String userId);

    @Query("SELECT u FROM User u where u.role = 'EMPLOYEE'")
    List<User> findAllEmployees();

}
