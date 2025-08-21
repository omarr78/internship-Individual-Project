package com.internship.task_management_system.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.internship.task_management_system.dto.task.TaskRequestDto;
import com.internship.task_management_system.emuns.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDate due_date;

    @JsonIgnore // to avoid Infinite recursion
    @ManyToOne
    @JoinColumn(name = "user_id" , nullable=false)
    private User user;

    public Task() {}

    public Task(TaskRequestDto taskRequestDto) {
        this.title = taskRequestDto.getTitle();
        this.description = taskRequestDto.getDescription();
        this.status = taskRequestDto.getStatus() != null ? taskRequestDto.getStatus() : TaskStatus.TODO;
        this.due_date = taskRequestDto.getDue_date() != null ? taskRequestDto.getDue_date() : LocalDate.now();
    }

}
