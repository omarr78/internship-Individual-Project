package com.internship.task_management_system.controllers;

import com.internship.task_management_system.entities.Task;
import com.internship.task_management_system.emuns.TaskStatus;
import com.internship.task_management_system.services.UserTaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class TaskController {

    private final UserTaskService userTaskService;

    public TaskController(UserTaskService userTaskService) {
        this.userTaskService = userTaskService;
    }

    @GetMapping("/{userId}/tasks")
    public List<Task> getTasks(
            @RequestParam(value = "status", required = false) TaskStatus status,
            @RequestParam(value = "title", required = false) String title,
            @PathVariable long userId
    ){
        return userTaskService.getUserTasks(userId,status,title);
    }

    @PostMapping("/{userId}/tasks")
    public ResponseEntity<Task> addTask(@PathVariable long userId, @RequestBody @Valid Task task){
        return userTaskService.addTaskToUser(userId,task);
    }

    @DeleteMapping("/{userId}/tasks/{taskId}")
    public String deleteTask(@PathVariable long userId, @PathVariable long taskId){
        return userTaskService.deleteTask(userId,taskId);
    }

    @PutMapping("/{userId}/tasks/{taskId}")
    public String updateTask(@PathVariable long userId, @PathVariable long taskId, @RequestBody Task updatedTask){
        return userTaskService.updateTask(userId,taskId,updatedTask);
    }
}
