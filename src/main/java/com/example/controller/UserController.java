package com.example.controller;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.dto.UserDTO;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    public CollectionModel<UserDTO> getAll() {
        List<UserDTO> users = userService.getAll();
        users.forEach(user ->
            user.add(linkTo(methodOn(UserController.class)
            .getById(user.getId())).withSelfRel())
        );
        return CollectionModel.of(users,
            linkTo(methodOn(UserController.class).getAll()).withSelfRel()
        );
    }

    @Operation(summary = "Получить пользователя по id")
    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        UserDTO user = userService.getById(id);

        user.add(linkTo(methodOn(UserController.class).getById(id)).withSelfRel());
        user.add(linkTo(methodOn(UserController.class).getAll()).withRel("all-users"));

        return user;
    }

    @Operation(summary = "Создать пользователя")
    @PostMapping
    public UserDTO create(@RequestBody UserDTO dto) {
        return userService.create(dto);
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}