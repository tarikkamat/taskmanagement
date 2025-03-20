package com.tarikkamat.taskmanagement.repository;

import com.tarikkamat.taskmanagement.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    Department findByManager_Id(UUID managerId);
} 