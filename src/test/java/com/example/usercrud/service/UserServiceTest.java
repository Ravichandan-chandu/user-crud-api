package com.example.usercrud.service;

import com.example.usercrud.dto.UserDTO;
import com.example.usercrud.entity.User;
import com.example.usercrud.exception.DuplicateEmailException;
import com.example.usercrud.exception.ResourceNotFoundException;
import com.example.usercrud.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User("John Doe", "john@example.com", "1234567890");
        user.setId(1L);

        userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setPhone("1234567890");
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserDTO createdUser = userService.createUser(userDTO);

        // Assert
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getName()).isEqualTo("John Doe");
        assertThat(createdUser.getEmail()).isEqualTo("john@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_DuplicateEmail() {
        // Arrange
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(userDTO))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        User user2 = new User("Jane Doe", "jane@example.com", "0987654321");
        user2.setId(2L);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        // Act
        List<UserDTO> users = userService.getAllUsers();

        // Assert
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getName()).isEqualTo("John Doe");
        assertThat(users.get(1).getName()).isEqualTo("Jane Doe");
    }

    @Test
    void testGetUserById_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        UserDTO foundUser = userService.getUserById(1L);

        // Assert
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("John Doe");
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        UserDTO updateDTO = new UserDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setEmail("john@example.com");
        updateDTO.setPhone("9999999999");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserDTO updatedUser = userService.updateUser(1L, updateDTO);

        // Assert
        assertThat(updatedUser).isNotNull();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.updateUser(1L, userDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void testUpdateUser_DuplicateEmail() {
        // Arrange
        UserDTO updateDTO = new UserDTO();
        updateDTO.setName("John Doe");
        updateDTO.setEmail("newemail@example.com");
        updateDTO.setPhone("1234567890");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("newemail@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.updateUser(1L, updateDTO))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void testDeleteUser_Success() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userService.deleteUser(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}
