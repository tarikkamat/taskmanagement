package com.tarikkamat.taskmanagement.repository;

import com.tarikkamat.taskmanagement.entity.Attachment;
import com.tarikkamat.taskmanagement.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AttachmentRepositoryTest {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Task task;
    private Attachment attachment1;
    private Attachment attachment2;

    @BeforeEach
    void setUp() {
        // Test verilerini hazırla
        task = new Task();
        task.setTitle("Test Task");
        task.setUserStoryDescription("Test Description");
        entityManager.persist(task);

        attachment1 = new Attachment();
        attachment1.setFileName("test1.txt");
        attachment1.setFilePath("/path/to/test1.txt");
        attachment1.setTask(task);
        entityManager.persist(attachment1);

        attachment2 = new Attachment();
        attachment2.setFileName("test2.txt");
        attachment2.setFilePath("/path/to/test2.txt");
        attachment2.setTask(task);
        entityManager.persist(attachment2);

        entityManager.flush();
    }

    @Test
    void findAllByTaskId_ShouldReturnAttachments() {
        // when
        List<Attachment> foundAttachments = attachmentRepository.findAllByTaskId(task.getId());

        // then
        assertThat(foundAttachments).hasSize(2);
        assertThat(foundAttachments).contains(attachment1, attachment2);
    }

    @Test
    void findAllByTaskId_WhenTaskDoesNotExist_ShouldReturnEmptyList() {
        // when
        List<Attachment> foundAttachments = attachmentRepository.findAllByTaskId(UUID.randomUUID());

        // then
        assertThat(foundAttachments).isEmpty();
    }
} 