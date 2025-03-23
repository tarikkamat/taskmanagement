package com.tarikkamat.taskmanagement.repository;

import com.tarikkamat.taskmanagement.entity.Department;
import com.tarikkamat.taskmanagement.entity.Project;
import com.tarikkamat.taskmanagement.entity.Task;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.enums.Priority;
import com.tarikkamat.taskmanagement.enums.ProjectStatus;
import com.tarikkamat.taskmanagement.enums.TaskState;
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
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void shouldSaveTask() {
        // given
        Project project = createAndSaveProject();
        User assignee = createAndSaveUser("Görev Sorumlusu");

        Task task = new Task();
        task.setTitle("Test Görevi");
        task.setUserStoryDescription("Test görev açıklaması");
        task.setAcceptanceCriteria("Test kabul kriterleri");
        task.setState(TaskState.BACKLOG);
        task.setPriority(Priority.HIGH);
        task.setProject(project);
        task.setAssignee(assignee);
        task.setCreatedAt(new Date());

        // when
        Task savedTask = taskRepository.save(task);

        // then
        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Test Görevi");
        assertThat(savedTask.getProject().getId()).isEqualTo(project.getId());
        assertThat(savedTask.getAssignee().getId()).isEqualTo(assignee.getId());
    }

    @Test
    void shouldFindTasksByProjectId() {
        // given
        Project project = createAndSaveProject();
        User assignee = createAndSaveUser("Görev Sorumlusu");

        Task task1 = createTask("Görev 1", project, assignee, TaskState.BACKLOG, Priority.HIGH);
        Task task2 = createTask("Görev 2", project, assignee, TaskState.IN_PROGRESS, Priority.MEDIUM);
        taskRepository.saveAll(List.of(task1, task2));

        // when
        List<Task> tasks = taskRepository.findByProjectId(project.getId());

        // then
        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Görev 1", "Görev 2");
    }

    @Test
    void shouldFindTasksByAssigneeId() {
        // given
        Project project = createAndSaveProject();
        User assignee = createAndSaveUser("Görev Sorumlusu");

        Task task1 = createTask("Görev 1", project, assignee, TaskState.BACKLOG, Priority.HIGH);
        Task task2 = createTask("Görev 2", project, assignee, TaskState.IN_PROGRESS, Priority.MEDIUM);
        taskRepository.saveAll(List.of(task1, task2));

        // when
        List<Task> tasks = taskRepository.findByAssigneeId(assignee.getId());

        // then
        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Görev 1", "Görev 2");
    }

    @Test
    void shouldFindTasksByState() {
        // given
        Project project = createAndSaveProject();
        User assignee = createAndSaveUser("Görev Sorumlusu");

        Task task1 = createTask("Görev 1", project, assignee, TaskState.BACKLOG, Priority.HIGH);
        Task task2 = createTask("Görev 2", project, assignee, TaskState.IN_PROGRESS, Priority.MEDIUM);
        taskRepository.saveAll(List.of(task1, task2));

        // when
        List<Task> backlogTasks = taskRepository.findByState(TaskState.BACKLOG);
        List<Task> inProgressTasks = taskRepository.findByState(TaskState.IN_PROGRESS);

        // then
        assertThat(backlogTasks).hasSize(1);
        assertThat(backlogTasks.get(0).getTitle()).isEqualTo("Görev 1");
        assertThat(inProgressTasks).hasSize(1);
        assertThat(inProgressTasks.get(0).getTitle()).isEqualTo("Görev 2");
    }

    @Test
    void shouldFindTasksByPriority() {
        // given
        Project project = createAndSaveProject();
        User assignee = createAndSaveUser("Görev Sorumlusu");

        Task task1 = createTask("Görev 1", project, assignee, TaskState.BACKLOG, Priority.HIGH);
        Task task2 = createTask("Görev 2", project, assignee, TaskState.IN_PROGRESS, Priority.MEDIUM);
        taskRepository.saveAll(List.of(task1, task2));

        // when
        List<Task> highPriorityTasks = taskRepository.findByPriority(Priority.HIGH);
        List<Task> mediumPriorityTasks = taskRepository.findByPriority(Priority.MEDIUM);

        // then
        assertThat(highPriorityTasks).hasSize(1);
        assertThat(highPriorityTasks.get(0).getTitle()).isEqualTo("Görev 1");
        assertThat(mediumPriorityTasks).hasSize(1);
        assertThat(mediumPriorityTasks.get(0).getTitle()).isEqualTo("Görev 2");
    }

    @Test
    void shouldFindNonDeletedTasks() {
        // given
        Project project = createAndSaveProject();
        User assignee = createAndSaveUser("Görev Sorumlusu");

        Task task1 = createTask("Görev 1", project, assignee, TaskState.BACKLOG, Priority.HIGH);
        Task task2 = createTask("Görev 2", project, assignee, TaskState.IN_PROGRESS, Priority.MEDIUM);
        taskRepository.saveAll(List.of(task1, task2));

        task1.setDeletedAt(new Date()); // task1'i sil
        taskRepository.save(task1);

        // when
        List<Task> nonDeletedTasks = taskRepository.findByDeletedAtIsNull();

        // then
        assertThat(nonDeletedTasks).hasSize(1);
        assertThat(nonDeletedTasks.get(0).getTitle()).isEqualTo("Görev 2");
    }

    private Project createAndSaveProject() {
        Department department = new Department();
        department.setName("Test Departmanı");
        department.setCreatedAt(new Date());
        Department savedDepartment = departmentRepository.save(department);

        User projectManager = createAndSaveUser("Proje Yöneticisi");

        Project project = new Project();
        project.setTitle("Test Projesi");
        project.setDescription("Test Proje Açıklaması");
        project.setDepartment(savedDepartment);
        project.setProjectManager(projectManager);
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setCreatedAt(new Date());
        return projectRepository.save(project);
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

    private Task createTask(String title, Project project, User assignee, TaskState state, Priority priority) {
        Task task = new Task();
        task.setTitle(title);
        task.setUserStoryDescription(title + " açıklaması");
        task.setAcceptanceCriteria(title + " kabul kriterleri");
        task.setState(state);
        task.setPriority(priority);
        task.setProject(project);
        task.setAssignee(assignee);
        task.setCreatedAt(new Date());
        return task;
    }
} 