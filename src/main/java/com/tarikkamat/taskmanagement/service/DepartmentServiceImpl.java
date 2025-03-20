package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.DepartmentDto;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.mapper.DepartmentMapper;
import com.tarikkamat.taskmanagement.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentDto getDepartment() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        DepartmentDto departmentDto = departmentMapper.toDto(departmentRepository.findByManager_Id(currentUser.getId()));

        if (departmentDto == null) {
            throw new RuntimeException("Department not found");
        }

        return departmentDto;
    }
}
