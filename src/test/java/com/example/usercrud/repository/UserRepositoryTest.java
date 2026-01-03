package com.example.usercrud.repository;

import com.example.usercrud.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User("John Doe", "john@example.com", "1234567890");

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("John Doe");
        assertThat(savedUser.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testFindByEmail() {
        // Arrange
        User user = new User("Jane Doe", "jane@example.com", "0987654321");
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("jane@example.com");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Jane Doe");
    }

    @Test
    void testExistsByEmail() {
        // Arrange
        User user = new User("Bob Smith", "bob@example.com", "1111111111");
        userRepository.save(user);

        // Act & Assert
        assertThat(userRepository.existsByEmail("bob@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@example.com")).isFalse();
    }

    @Test
    void testFindAllUsers() {
        // Arrange
        userRepository.save(new User("User 1", "user1@example.com", "1111111111"));
        userRepository.save(new User("User 2", "user2@example.com", "2222222222"));

        // Act
        List<User> users = userRepository.findAll();

        // Assert
        assertThat(users).hasSize(2);
    }

    @Test
    void testDeleteUser() {
        // Arrange
        User user = new User("Delete Me", "delete@example.com", "3333333333");
        User savedUser = userRepository.save(user);

        // Act
        userRepository.deleteById(savedUser.getId());

        // Assert
        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }

    @Test
    void testUpdateUser() {
        // Arrange
        User user = new User("Old Name", "old@example.com", "4444444444");
        User savedUser = userRepository.save(user);

        // Act
        savedUser.setName("New Name");
        savedUser.setEmail("new@example.com");
        User updatedUser = userRepository.save(savedUser);

        // Assert
        assertThat(updatedUser.getName()).isEqualTo("New Name");
        assertThat(updatedUser.getEmail()).isEqualTo("new@example.com");
    }
}
