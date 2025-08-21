package com.internship.task_management_system.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.internship.task_management_system.dto.user.UserRequestDto;
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
    private String username;
    private String password;

    private String role = Role.USER.name(); // default role

    // orphanRemoval = true -> when no longer referenced by their parent entity,
    // should be automatically deleted from the database.

    @JsonIgnore // to avoid Infinite recursion
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;



    public User(){}

    public User(UserRequestDto userRequestDto) {
        this.username = userRequestDto.getUsername();
        this.password = userRequestDto.getPassword();
    }
}
