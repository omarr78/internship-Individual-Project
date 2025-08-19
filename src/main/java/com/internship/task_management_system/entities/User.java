package com.internship.task_management_system.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @JsonIgnore // to avoid Infinite recursion
    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

}
