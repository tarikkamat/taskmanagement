package com.tarikkamat.taskmanagement.controller;

import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.ProjectDto;
import com.tarikkamat.taskmanagement.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
