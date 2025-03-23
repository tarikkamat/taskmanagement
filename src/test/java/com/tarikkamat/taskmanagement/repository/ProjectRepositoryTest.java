package com.tarikkamat.taskmanagement.repository;

import com.tarikkamat.taskmanagement.entity.Department;
import com.tarikkamat.taskmanagement.entity.Project;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.enums.ProjectStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveProject() {
        // given
        Department department = createAndSaveDepartment("Test Departmanı");
        User projectManager = createAndSaveUser("Proje Yöneticisi");

        Project project = new Project();
        project.setTitle("Test Projesi");
        project.setDescription("Test Proje Açıklaması");
        project.setDepartment(department);
        project.setProjectManager(projectManager);
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setCreatedAt(new Date());

        // when
        Project savedProject = projectRepository.save(project);

        // then
        assertThat(savedProject).isNotNull();
        assertThat(savedProject.getId()).isNotNull();
        assertThat(savedProject.getTitle()).isEqualTo("Test Projesi");
        assertThat(savedProject.getDepartment().getId()).isEqualTo(department.getId());
        assertThat(savedProject.getProjectManager().getId()).isEqualTo(projectManager.getId());
    }

    @Test
    void shouldFindProjectById() {
        // given
        Department department = createAndSaveDepartment("Test Departmanı");
        User projectManager = createAndSaveUser("Proje Yöneticisi");

        Project project = new Project();
        project.setTitle("Test Projesi");
        project.setDescription("Test Proje Açıklaması");
        project.setDepartment(department);
        project.setProjectManager(projectManager);
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setCreatedAt(new Date());

        Project savedProject = projectRepository.save(project);

        // when
        Optional<Project> foundProject = projectRepository.findById(savedProject.getId());

        // then
        assertThat(foundProject).isPresent();
        assertThat(foundProject.get().getTitle()).isEqualTo("Test Projesi");
    }

    @Test
    void shouldFindProjectsByDepartmentId() {
        // given
        Department department = createAndSaveDepartment("Test Departmanı");
        User projectManager = createAndSaveUser("Proje Yöneticisi");

        Project project1 = new Project();
        project1.setTitle("Proje 1");
        project1.setDepartment(department);
        project1.setProjectManager(projectManager);
        project1.setStatus(ProjectStatus.IN_PROGRESS);
        project1.setCreatedAt(new Date());

        Project project2 = new Project();
        project2.setTitle("Proje 2");
        project2.setDepartment(department);
        project2.setProjectManager(projectManager);
        project2.setStatus(ProjectStatus.IN_PROGRESS);
        project2.setCreatedAt(new Date());

        projectRepository.saveAll(List.of(project1, project2));

        // when
        List<Project> projects = projectRepository.findByDepartment_Id(department.getId());

        // then
        assertThat(projects).hasSize(2);
        assertThat(projects).extracting(Project::getTitle)
                .containsExactlyInAnyOrder("Proje 1", "Proje 2");
    }

    @Test
    void shouldFindProjectByIdAndDepartmentId() {
        // given
        Department department = createAndSaveDepartment("Test Departmanı");
        User projectManager = createAndSaveUser("Proje Yöneticisi");

        Project project = new Project();
        project.setTitle("Test Projesi");
        project.setDepartment(department);
        project.setProjectManager(projectManager);
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setCreatedAt(new Date());

        Project savedProject = projectRepository.save(project);

        // when
        Project foundProject = projectRepository.findByIdAndDepartment_Id(
                savedProject.getId(), department.getId());

        // then
        assertThat(foundProject).isNotNull();
        assertThat(foundProject.getTitle()).isEqualTo("Test Projesi");
        assertThat(foundProject.getDepartment().getId()).isEqualTo(department.getId());
    }

    @Test
    void shouldDeleteProject() {
        // given
        Department department = createAndSaveDepartment("Test Departmanı");
        User projectManager = createAndSaveUser("Proje Yöneticisi");

        Project project = new Project();
        project.setTitle("Silinecek Proje");
        project.setDepartment(department);
        project.setProjectManager(projectManager);
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setCreatedAt(new Date());

        Project savedProject = projectRepository.save(project);

        // when
        projectRepository.deleteById(savedProject.getId());

        // then
        Optional<Project> deletedProject = projectRepository.findById(savedProject.getId());
        assertThat(deletedProject).isEmpty();
    }

    private Department createAndSaveDepartment(String name) {
        Department department = new Department();
        department.setName(name);
        department.setCreatedAt(new Date());
        return departmentRepository.save(department);
    }

    private User createAndSaveUser(String fullName) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(fullName.toLowerCase().replace(" ", ".") + "@test.com");
        user.setUsername(fullName.toLowerCase().replace(" ", ""));
        user.setPassword("test123");
        user.setCreatedAt(new Date());
        return userRepository.save(user);
    }
} 