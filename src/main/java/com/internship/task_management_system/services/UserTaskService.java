package com.internship.task_management_system.services;

import com.internship.task_management_system.entities.Task;
import com.internship.task_management_system.emuns.TaskStatus;
import com.internship.task_management_system.entities.User;
import com.internship.task_management_system.exceptions.ResourceAlreadyExistException;
import com.internship.task_management_system.exceptions.ResourceNotFoundException;
import com.internship.task_management_system.jpa.TaskRepository;
import com.internship.task_management_system.jpa.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserTaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public UserTaskService(TaskRepository taskRepository, UserService userService, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public void checkUniqueTask(User user, String title) {
        user.getTasks()
                .stream()
                .filter(task -> task.getTitle().equals(title))
                .findFirst()
                .ifPresent(task -> {
                    throw new ResourceAlreadyExistException(
                            "Task with title '" + title + "' already exists for user " + user.getId()
                    );
                });
    }


    public Task checkTaskExist(User user, long taskId) {
        return user.getTasks().stream()
                .filter(task -> task.getId() == taskId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task with id " + taskId + " not found for user " + user.getId()
                ));
    }



    public List<Task> getUserTasks(long userId, TaskStatus status, String title) {
        if(status == null && title == null) {
            return taskRepository.findByUser_Id(userId);
        }
        else if(status != null && title == null) {
            return taskRepository.findByUser_IdAndStatus(userId, status);
        }
        else if(status == null && title != null) {
            return taskRepository.findByUser_IdAndTitleContainingIgnoreCase(userId,title);
        }
        else{
            return taskRepository.
                    findByUser_IdAndStatusAndTitleContainingIgnoreCase(userId, status, title);
        }
    }



    public ResponseEntity<Task> addTaskToUser(long userId, Task task) {
        User user = userService.checkUserExist(userId);

        checkUniqueTask(user,task.getTitle());


        task.setUser(user);
        Task newTask = taskRepository.save(task);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTask.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    public String deleteTask(long userId, long taskId) {
        User user = userService.checkUserExist(userId);
        Task task = checkTaskExist(user, taskId);

        // break the relationship
        user.getTasks().remove(task);
        task.setUser(null);

        taskRepository.delete(task);
        return "Task deleted successfully";
    }

    public String updateTask(long userId, long taskId, Task updatedTask) {
        User user = userService.checkUserExist(userId);
        Task task = checkTaskExist(user, taskId);

        if (updatedTask.getTitle() != null) {
            checkUniqueTask(user, updatedTask.getTitle()); // check if updated task title already exists?
            task.setTitle(updatedTask.getTitle());
        }

        if (updatedTask.getDescription() != null) task.setDescription(updatedTask.getDescription());
        if (updatedTask.getStatus() != null) task.setStatus(updatedTask.getStatus());
        if (updatedTask.getDue_date() != null) {
            if (updatedTask.getDue_date().isBefore(LocalDate.now())) {
                throw new RuntimeException("Due date must be in the future");
            }
            task.setDue_date(updatedTask.getDue_date());
        }

        taskRepository.save(task);
        return "Task updated successfully";
    }
}
