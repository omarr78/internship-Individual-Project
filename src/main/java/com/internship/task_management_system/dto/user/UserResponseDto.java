package com.internship.task_management_system.dto.user;

import com.internship.task_management_system.entities.User;
import lombok.Data;

@Data
public class UserResponseDto {
    private long id;
    private String username;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
