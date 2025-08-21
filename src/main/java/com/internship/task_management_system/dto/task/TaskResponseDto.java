package com.internship.task_management_system.dto.task;

import com.internship.task_management_system.emuns.TaskStatus;
import com.internship.task_management_system.entities.Task;
import lombok.Data;

import java.time.LocalDate;


@Data
public class TaskResponseDto {

    private long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate due_date;

    public TaskResponseDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.due_date = task.getDue_date();
    }

}
