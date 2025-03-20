package com.tarikkamat.taskmanagement.repository;

import com.tarikkamat.taskmanagement.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByDepartment_Id(UUID departmentId);
}
