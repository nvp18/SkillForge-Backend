package com.skillforge.backend.repository;

import com.skillforge.backend.entity.Concerns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConcernRepository extends JpaRepository<Concerns, UUID> {

    List<Concerns> findByUserUserId(String employeeID);

    Optional<Concerns> findById(String id);
}
