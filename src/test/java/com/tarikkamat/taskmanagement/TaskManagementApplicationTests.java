package com.tarikkamat.taskmanagement;

import com.tarikkamat.taskmanagement.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskManagementApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void contextLoads() {
        // Uygulama bağlamının yüklendiğini kontrol et
        assertThat(applicationContext).isNotNull();

        // Tüm repository'lerin doğru şekilde yüklendiğini kontrol et
        assertThat(taskRepository).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(projectRepository).isNotNull();
        assertThat(departmentRepository).isNotNull();
        assertThat(commentRepository).isNotNull();
    }

    @Test
    void applicationStartsSuccessfully() {
        // Bu test metodunun başarılı olması, uygulamanın başarıyla başlatıldığını gösterir
        assertThat(true).isTrue();
    }
}
