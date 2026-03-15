package com.example.service;

import com.example.dao.UserDAO;
import com.example.entity.User;

import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void createUser(User user) {
        userDAO.create(user);
    }

    public User getUser(Long id) {
        return userDAO.getById(id);
    }

    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }

    public void deleteUser(Long id) {
        userDAO.delete(id);
    }
}