package com.internship.task_management_system.services;

import com.internship.task_management_system.dto.task.TaskRequestDto;
import com.internship.task_management_system.dto.task.TaskResponseDto;
import com.internship.task_management_system.entities.Task;
import com.internship.task_management_system.emuns.TaskStatus;
import com.internship.task_management_system.entities.User;
import com.internship.task_management_system.exceptions.ResourceAlreadyExistException;
import com.internship.task_management_system.exceptions.ResourceNotFoundException;
import com.internship.task_management_system.jpa.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public UserTaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    private void checkUniqueTask(User user, String title, long taskId) { // check if title already exists
        user.getTasks()
                .stream()
                .filter(task -> task.getTitle().equals(title) && (taskId == 0L || task.getId() != taskId))
                .findFirst()
                .ifPresent(task -> {
                    throw new ResourceAlreadyExistException(
                            "Task with title " + title + " already exists for user " + user.getId()
                    );
                });
    }


    private Task checkTaskExist(User user, long taskId) {
        return user.getTasks().stream()
                .filter(task -> task.getId() == taskId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task with id " + taskId + " not found for user " + user.getId()
                ));
    }


    public List<TaskResponseDto> getUserTasks(long userId, TaskStatus status, String title) {
        List<Task> tasks;

        if (status == null && title == null) {
            tasks = taskRepository.findByUser_Id(userId);
        } else if (status != null && title == null) {
            tasks = taskRepository.findByUser_IdAndStatus(userId, status);
        } else if (status == null && title != null) {
            tasks = taskRepository.findByUser_IdAndTitleContainingIgnoreCase(userId, title);
        } else {
            tasks = taskRepository.
                    findByUser_IdAndStatusAndTitleContainingIgnoreCase(userId, status, title);
        }
        return tasks.stream().map(TaskResponseDto::new).toList();
    }

    public TaskResponseDto getTask(long userId, long taskId) {
        User user = userService.checkUserExist(userId);
        Task task = checkTaskExist(user, taskId);

        return new TaskResponseDto(task);
    }


    public TaskResponseDto addTaskToUser(long userId, TaskRequestDto task) {
        User user = userService.checkUserExist(userId);
        checkUniqueTask(user, task.getTitle(), 0L); // 0L default value for a long

        Task newTask = new Task(task);
        newTask.setUser(user);
        return new TaskResponseDto(taskRepository.save(newTask));
    }

    public void deleteTask(long userId, long taskId) {
        User user = userService.checkUserExist(userId);
        Task task = checkTaskExist(user, taskId);

        // break the relationship
        user.getTasks().remove(task);
        task.setUser(null);

        taskRepository.delete(task);
    }

    public TaskResponseDto updateTask(long userId, long taskId, TaskRequestDto updatedTask) {
        User user = userService.checkUserExist(userId);
        Task task = checkTaskExist(user, taskId);
        if (updatedTask.getTitle() != null) {
            checkUniqueTask(user, updatedTask.getTitle(), task.getId()); // check if updated task title already exists?
            task.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getDescription() != null) task.setDescription(updatedTask.getDescription());
        if (updatedTask.getStatus() != null) task.setStatus(updatedTask.getStatus());
        if (updatedTask.getDue_date() != null) {
            task.setDue_date(updatedTask.getDue_date());
        }
        taskRepository.save(task);
        return new TaskResponseDto(task);
    }
}
