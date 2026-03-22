package com.example.controller;

import com.example.dto.UserDTO;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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
    private UserService service;

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        UserDTO request = new UserDTO();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setAge(30);

        UserDTO response = new UserDTO();
        response.setId(1L);
        response.setName("Alice");
        response.setEmail("alice@example.com");
        response.setAge(30);
        response.setCreatedAt(LocalDateTime.of(2026, 3, 22, 12, 0));

        when(service.create(any(UserDTO.class))).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.createdAt").exists());

        verify(service, times(1)).create(any(UserDTO.class));
    }

    @Test
    void getAllUsers_ShouldReturnList() throws Exception {
        UserDTO user1 = new UserDTO();
        user1.setId(1L);
        user1.setName("Alice");
        user1.setEmail("alice@example.com");
        user1.setAge(30);
        user1.setCreatedAt(LocalDateTime.of(2026, 3, 22, 12, 0));

        UserDTO user2 = new UserDTO();
        user2.setId(2L);
        user2.setName("Bob");
        user2.setEmail("bob@example.com");
        user2.setAge(25);
        user2.setCreatedAt(LocalDateTime.of(2026, 3, 22, 13, 0));

        when(service.getAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Bob"));

        verify(service, times(1)).getAll();
    }

    @Test
    void getById_ShouldReturnUser() throws Exception {
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setAge(30);
        user.setCreatedAt(LocalDateTime.of(2026, 3, 22, 12, 0));

        when(service.getById(eq(1L))).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));

        verify(service, times(1)).getById(eq(1L));
    }

    @Test
    void deleteUser_ShouldReturnOk() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).delete(eq(1L));
    }
}