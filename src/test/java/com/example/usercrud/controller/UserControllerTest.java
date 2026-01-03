package com.example.usercrud.controller;

import com.example.usercrud.dto.UserDTO;
import com.example.usercrud.exception.DuplicateEmailException;
import com.example.usercrud.exception.ResourceNotFoundException;
import com.example.usercrud.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setPhone("1234567890");
    }

    @Test
    void testCreateUser_Success() throws Exception {
        // Arrange
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void testCreateUser_ValidationError() throws Exception {
        // Arrange
        UserDTO invalidUser = new UserDTO();
        invalidUser.setName("J"); // Too short
        invalidUser.setEmail("invalid-email"); // Invalid email format

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Arrange
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setName("Jane Doe");
        userDTO2.setEmail("jane@example.com");
        userDTO2.setPhone("0987654321");

        List<UserDTO> users = Arrays.asList(userDTO, userDTO2);
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(userDTO);

        // Act & Assert
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenThrow(new ResourceNotFoundException("User not found with id: 1"));

        // Act & Assert
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        // Arrange
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(userDTO);

        // Act & Assert
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testUpdateUser_DuplicateEmail() throws Exception {
        // Arrange
        when(userService.updateUser(eq(1L), any(UserDTO.class)))
                .thenThrow(new DuplicateEmailException("Email already exists"));

        // Act & Assert
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        // Arrange
        doNothing().when(userService).deleteUser(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("User not found with id: 1"))
                .when(userService).deleteUser(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());
    }
}
