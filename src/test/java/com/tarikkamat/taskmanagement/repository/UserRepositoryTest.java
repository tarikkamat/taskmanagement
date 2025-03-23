package com.tarikkamat.taskmanagement.repository;

import com.tarikkamat.taskmanagement.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() {
        // given
        User user = new User();
        user.setFullName("Test Kullanıcı");
        user.setEmail("test@test.com");
        user.setUsername("testuser");
        user.setPassword("test123");
        user.setCreatedAt(new Date());

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getFullName()).isEqualTo("Test Kullanıcı");
        assertThat(savedUser.getEmail()).isEqualTo("test@test.com");
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldFindUserByUsername() {
        // given
        User user = new User();
        user.setFullName("Test Kullanıcı");
        user.setEmail("test@test.com");
        user.setUsername("testuser");
        user.setPassword("test123");
        user.setCreatedAt(new Date());
        userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
        assertThat(foundUser.get().getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void shouldNotFindUserByNonExistentUsername() {
        // when
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // then
        assertThat(foundUser).isEmpty();
    }

    @Test
    void shouldFindUserByEmail() {
        // given
        User user = new User();
        user.setFullName("Test Kullanıcı");
        user.setEmail("test@test.com");
        user.setUsername("testuser");
        user.setPassword("test123");
        user.setCreatedAt(new Date());
        userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findByEmailOrUsername("test@test.com");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void shouldFindUserByUsernameUsingEmailOrUsername() {
        // given
        User user = new User();
        user.setFullName("Test Kullanıcı");
        user.setEmail("test@test.com");
        user.setUsername("testuser");
        user.setPassword("test123");
        user.setCreatedAt(new Date());
        userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findByEmailOrUsername("testuser");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldNotFindUserByNonExistentEmailOrUsername() {
        // when
        Optional<User> foundUser = userRepository.findByEmailOrUsername("nonexistent");

        // then
        assertThat(foundUser).isEmpty();
    }
} 