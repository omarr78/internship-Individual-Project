package com.internship.task_management_system.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.internship.task_management_system.emuns.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private long id;

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    private String role = Role.USER.name(); // default role

    @JsonIgnore // to avoid Infinite recursion
    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

}
