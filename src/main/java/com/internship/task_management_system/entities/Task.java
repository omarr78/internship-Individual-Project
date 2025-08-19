package com.internship.task_management_system.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.internship.task_management_system.emuns.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "description is required")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "task status is required")
    private TaskStatus status;

    @NotNull(message = "due date is required")
    @FutureOrPresent(message = "due date must be in the future")
    private LocalDate due_date;

    // to avoid Infinite recursion
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id" , nullable=false)
    private User user;


}
