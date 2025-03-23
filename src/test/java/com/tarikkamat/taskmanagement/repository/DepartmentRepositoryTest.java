package com.tarikkamat.taskmanagement.repository;

import com.tarikkamat.taskmanagement.entity.Department;
import com.tarikkamat.taskmanagement.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveDepartment() {
        // given
        Department department = new Department();
        department.setName("Test Departmanı");
        department.setCreatedAt(new Date());

        // when
        Department savedDepartment = departmentRepository.save(department);

        // then
        assertThat(savedDepartment).isNotNull();
        assertThat(savedDepartment.getId()).isNotNull();
        assertThat(savedDepartment.getName()).isEqualTo("Test Departmanı");
    }

    @Test
    void shouldFindDepartmentById() {
        // given
        Department department = new Department();
        department.setName("Test Departmanı");
        department.setCreatedAt(new Date());
        Department savedDepartment = departmentRepository.save(department);

        // when
        Optional<Department> foundDepartment = departmentRepository.findById(savedDepartment.getId());

        // then
        assertThat(foundDepartment).isPresent();
        assertThat(foundDepartment.get().getName()).isEqualTo("Test Departmanı");
    }

    @Test
    void shouldFindAllDepartments() {
        // given
        Department department1 = new Department();
        department1.setName("Departman 1");
        department1.setCreatedAt(new Date());

        Department department2 = new Department();
        department2.setName("Departman 2");
        department2.setCreatedAt(new Date());

        departmentRepository.saveAll(List.of(department1, department2));

        // when
        List<Department> departments = departmentRepository.findAll();

        // then
        assertThat(departments).hasSize(2);
        assertThat(departments).extracting(Department::getName)
                .containsExactlyInAnyOrder("Departman 1", "Departman 2");
    }

    @Test
    void shouldFindDepartmentByManagerId() {
        // given
        User manager = new User();
        manager.setFullName("Test Yönetici");
        manager.setEmail("yonetici@test.com");
        manager.setUsername("testmanager");
        manager.setPassword("test123");
        manager.setCreatedAt(new Date());
        User savedManager = userRepository.save(manager);

        Department department = new Department();
        department.setName("Test Departmanı");
        department.setManager(savedManager);
        department.setCreatedAt(new Date());
        departmentRepository.save(department);

        // when
        Department foundDepartment = departmentRepository.findByManager_Id(savedManager.getId());

        // then
        assertThat(foundDepartment).isNotNull();
        assertThat(foundDepartment.getName()).isEqualTo("Test Departmanı");
        assertThat(foundDepartment.getManager().getId()).isEqualTo(savedManager.getId());
    }

    @Test
    void shouldDeleteDepartment() {
        // given
        Department department = new Department();
        department.setName("Silinecek Departman");
        department.setCreatedAt(new Date());
        Department savedDepartment = departmentRepository.save(department);

        // when
        departmentRepository.deleteById(savedDepartment.getId());

        // then
        Optional<Department> deletedDepartment = departmentRepository.findById(savedDepartment.getId());
        assertThat(deletedDepartment).isEmpty();
    }
} 