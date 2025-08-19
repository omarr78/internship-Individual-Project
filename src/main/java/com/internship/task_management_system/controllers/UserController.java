package com.internship.task_management_system.controllers;

import com.internship.task_management_system.entities.User;
import com.internship.task_management_system.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public User getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    @PutMapping("{userId}")
    public ResponseEntity<User> updateUser(@PathVariable int userId, @RequestBody @Valid User updatedUser) {
        return userService.updateUser(userId,updatedUser);
    }

    @DeleteMapping("{userId}")
    public String deleteUser(@PathVariable int userId) {
        return userService.deleteUser(userId);
    }
}
