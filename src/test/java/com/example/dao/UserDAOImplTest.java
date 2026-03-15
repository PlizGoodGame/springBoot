package com.example.dao;

import com.example.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDAOImplTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private UserDAO userDAO;

    private static SessionFactory sessionFactory;

    @BeforeAll
    static void setUpSessionFactory() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        properties.setProperty("hibernate.connection.username", postgres.getUsername());
        properties.setProperty("hibernate.connection.password", postgres.getPassword());
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.format_sql", "false");
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    static void tearDownSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOImpl(sessionFactory);
    }

    @AfterEach
    void tearDown() {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testCreate() {
        User user = new User("John Doe", "john@example.com", 30);
        userDAO.create(user);

        assertNotNull(user.getId());
    }

    @Test
    void testGetById() {
        User user = new User("Jane Doe", "jane@example.com", 25);
        userDAO.create(user);
        Long id = user.getId();

        User retrieved = userDAO.getById(id);
        assertNotNull(retrieved);
        assertEquals("Jane Doe", retrieved.getName());
        assertEquals("jane@example.com", retrieved.getEmail());
        assertEquals(25, retrieved.getAge());
    }

    @Test
    void testGetAll() {
        User user1 = new User("User1", "user1@example.com", 20);
        User user2 = new User("User2", "user2@example.com", 30);
        userDAO.create(user1);
        userDAO.create(user2);

        List<User> users = userDAO.getAll();
        assertEquals(2, users.size());
    }

    @Test
    void testUpdate() {
        User user = new User("Old Name", "old@example.com", 40);
        userDAO.create(user);
        Long id = user.getId();

        user.setName("New Name");
        user.setEmail("new@example.com");
        user.setAge(45);
        userDAO.update(user);

        User updated = userDAO.getById(id);
        assertEquals("New Name", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals(45, updated.getAge());
    }

    @Test
    void testDelete() {
        User user = new User("To Delete", "delete@example.com", 50);
        userDAO.create(user);
        Long id = user.getId();

        userDAO.delete(id);

        User deleted = userDAO.getById(id);
        assertNull(deleted);
    }

    @Test
    void testGetByIdNotFound() {
        User retrieved = userDAO.getById(999L);
        assertNull(retrieved);
    }
}