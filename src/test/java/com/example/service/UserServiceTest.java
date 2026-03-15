package com.example.service;

import com.example.dao.UserDAO;
import com.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserDAO userDAOMock;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userDAOMock);
    }

    @Test
    void testCreateUser() {
        
        User user = new User("John Doe", "john@example.com", 30);

        userService.createUser(user);

        verify(userDAOMock, times(1)).create(user);
    }

    @Test
    void testGetUser() {
        
        Long userId = 1L;
        User expectedUser = new User("Jane Doe", "jane@example.com", 25);
        when(userDAOMock.getById(userId)).thenReturn(expectedUser);

        User actualUser = userService.getUser(userId);

        assertNotNull(actualUser);
        assertEquals("Jane Doe", actualUser.getName());
        assertEquals("jane@example.com", actualUser.getEmail());
        assertEquals(25, actualUser.getAge());
        verify(userDAOMock, times(1)).getById(userId);
    }

    @Test
    void testGetAllUsers() {
        
        List<User> expectedUsers = Arrays.asList(
            new User("User1", "user1@example.com", 20),
            new User("User2", "user2@example.com", 30)
        );
        when(userDAOMock.getAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertEquals("User1", actualUsers.get(0).getName());
        assertEquals("user1@example.com", actualUsers.get(0).getEmail());
        assertEquals(20, actualUsers.get(0).getAge());
        assertEquals("User2", actualUsers.get(1).getName());
        assertEquals("user2@example.com", actualUsers.get(1).getEmail());
        assertEquals(30, actualUsers.get(1).getAge());
        verify(userDAOMock, times(1)).getAll();
    }

    @Test
    void testUpdateUser() {
        
        User user = new User("Updated Name", "updated@example.com", 35);

        userService.updateUser(user);

        verify(userDAOMock, times(1)).update(user);
    }

    @Test
    void testDeleteUser() {
        
        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userDAOMock, times(1)).delete(userId);
    }

    @Test
    void testGetUserReturnsNullWhenNotFound() {
        Long userId = 999L;
        when(userDAOMock.getById(userId)).thenReturn(null);

        User actualUser = userService.getUser(userId);

        assertNull(actualUser);
        verify(userDAOMock, times(1)).getById(userId);
    }
}