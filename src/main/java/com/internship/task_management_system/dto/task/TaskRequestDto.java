package com.internship.task_management_system.dto.task;

import com.internship.task_management_system.emuns.TaskStatus;
import com.internship.task_management_system.validation.OnCreate;
import com.internship.task_management_system.validation.OnUpdate;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequestDto {

    @NotBlank(message = "title is required", groups = OnCreate.class)
    private String title;
    private String description;
    private TaskStatus status;
    @FutureOrPresent(message = "Due date must be today or in the future",
            groups = {OnCreate.class, OnUpdate.class})
    private LocalDate due_date;

}
