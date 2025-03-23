package com.tarikkamat.taskmanagement.repository;

import com.tarikkamat.taskmanagement.entity.Comment;
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
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldSaveComment() {
        // given
        Comment comment = new Comment();
        comment.setContent("Test yorum içeriği");
        comment.setCreatedAt(new Date());

        // when
        Comment savedComment = commentRepository.save(comment);

        // then
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("Test yorum içeriği");
    }

    @Test
    void shouldFindCommentById() {
        // given
        Comment comment = new Comment();
        comment.setContent("Test yorum içeriği");
        comment.setCreatedAt(new Date());
        Comment savedComment = commentRepository.save(comment);

        // when
        Optional<Comment> foundComment = commentRepository.findById(savedComment.getId());

        // then
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo("Test yorum içeriği");
    }

    @Test
    void shouldFindAllComments() {
        // given
        Comment comment1 = new Comment();
        comment1.setContent("Yorum 1");
        comment1.setCreatedAt(new Date());

        Comment comment2 = new Comment();
        comment2.setContent("Yorum 2");
        comment2.setCreatedAt(new Date());

        commentRepository.saveAll(List.of(comment1, comment2));

        // when
        List<Comment> comments = commentRepository.findAll();

        // then
        assertThat(comments).hasSize(2);
        assertThat(comments).extracting(Comment::getContent).containsExactlyInAnyOrder("Yorum 1", "Yorum 2");
    }

    @Test
    void shouldDeleteComment() {
        // given
        Comment comment = new Comment();
        comment.setContent("Silinecek yorum");
        comment.setCreatedAt(new Date());
        Comment savedComment = commentRepository.save(comment);

        // when
        commentRepository.deleteById(savedComment.getId());

        // then
        Optional<Comment> deletedComment = commentRepository.findById(savedComment.getId());
        assertThat(deletedComment).isEmpty();
    }
} 