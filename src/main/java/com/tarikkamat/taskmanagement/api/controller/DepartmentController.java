package com.tarikkamat.taskmanagement.api.controller;

import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.DepartmentDto;
import com.tarikkamat.taskmanagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/departments")
    public ResponseEntity<BaseResponse<DepartmentDto>> getDepartment() {
        try {
            DepartmentDto departmentDto = departmentService.getDepartment();
            return ResponseEntity.ok(new BaseResponse<>(true, "Department found", 200, departmentDto));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }
}
