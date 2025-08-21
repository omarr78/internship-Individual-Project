package com.internship.task_management_system.controllers;

import com.internship.task_management_system.dto.task.TaskRequestDto;
import com.internship.task_management_system.dto.task.TaskResponseDto;
import com.internship.task_management_system.emuns.TaskStatus;
import com.internship.task_management_system.services.UserTaskService;
import com.internship.task_management_system.validation.OnCreate;
import com.internship.task_management_system.validation.OnUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class TaskController {

    private final UserTaskService userTaskService;

    public TaskController(UserTaskService userTaskService) {
        this.userTaskService = userTaskService;
    }

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<TaskResponseDto>>  getTasks(
            @RequestParam(value = "status", required = false) TaskStatus status,
            @RequestParam(value = "title", required = false) String title,
            @PathVariable long userId
    ){
        return ResponseEntity.ok(userTaskService.getUserTasks(userId,status,title));
    }

    @GetMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable long taskId, @PathVariable long userId){
        return ResponseEntity.ok(userTaskService.getTask(userId,taskId));
    }

    @PostMapping("/{userId}/tasks")
    public ResponseEntity<TaskResponseDto> addTask(@PathVariable long userId, @RequestBody @Validated(OnCreate.class) TaskRequestDto task){
        TaskResponseDto createdTask = userTaskService.addTaskToUser(userId,task);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdTask);
    }

    @DeleteMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable long userId, @PathVariable long taskId){
        userTaskService.deleteTask(userId,taskId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable long userId,
                                                     @PathVariable long taskId,
                                                     @RequestBody @Validated(OnUpdate.class) TaskRequestDto updatedTask)
    {
        return ResponseEntity.ok(userTaskService.updateTask(userId,taskId,updatedTask));
    }
}
