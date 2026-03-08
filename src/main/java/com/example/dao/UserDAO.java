package com.example.dao;

import com.example.entity.User;
import java.util.List;

public interface UserDAO {
    void create(User user);

    User getById(Long id);

    List<User> getAll();

    void update(User user);

    void delete(Long id);
}
