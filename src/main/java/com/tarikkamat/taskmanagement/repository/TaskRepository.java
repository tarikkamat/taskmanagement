package com.tarikkamat.taskmanagement.repository;

import com.tarikkamat.taskmanagement.entity.Task;
import com.tarikkamat.taskmanagement.enums.Priority;
import com.tarikkamat.taskmanagement.enums.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByProjectId(UUID projectId);
    
    List<Task> findByAssigneeId(UUID assigneeId);
    
    List<Task> findByState(TaskState state);
    
    List<Task> findByPriority(Priority priority);
    
    List<Task> findByDeletedAtIsNull();
}
