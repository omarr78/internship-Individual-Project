package com.internship.task_management_system.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;

    public ErrorDetails(LocalDateTime now, String message, String description) {
        this.timestamp = now;
        this.message = message;
        this.details = description;
    }

}
