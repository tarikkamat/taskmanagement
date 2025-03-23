package com.tarikkamat.taskmanagement.api.controller;

import com.tarikkamat.taskmanagement.api.requests.project.UpdateProjectStatusRequest;
import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.ProjectDto;
import com.tarikkamat.taskmanagement.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/projects")
    public ResponseEntity<BaseResponse<List<ProjectDto>>> getAllProjects() {
        try {
            List<ProjectDto> projects = projectService.getAllProjects();
            return ResponseEntity.ok(new BaseResponse<>(true, "Projects found", 200, projects));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<BaseResponse<ProjectDto>> getProjectById(@PathVariable UUID projectId) {
        try {
            ProjectDto project = projectService.getProjectById(projectId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Project found", 200, project));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @PatchMapping("/projects/{id}/status")
    @PreAuthorize("hasAnyAuthority('GROUP_MANAGER','PROJECT_MANAGER')")
    public ResponseEntity<BaseResponse<ProjectDto>> updateTaskState(@PathVariable UUID id, @Valid @RequestBody UpdateProjectStatusRequest request) {
        try {
            ProjectDto projectDto = projectService.updateProjectStatus(id, request);
            return ResponseEntity.ok(new BaseResponse<>(true, "Project state updated", 200, projectDto));
        } catch (Exception e) {
            log.error("Project status not updated. Error: {}", e.getMessage());
            return ResponseEntity.status(400).body(new BaseResponse<>(false, e.getMessage(), 400, null));
        }
    }
}
