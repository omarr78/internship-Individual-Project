package com.internship.task_management_system.jpa;

import com.internship.task_management_system.entities.Task;
import com.internship.task_management_system.emuns.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser_Id(long userId);

    List<Task> findByUser_IdAndStatus(long userId, TaskStatus status);

    List<Task> findByUser_IdAndTitleContainingIgnoreCase(long userId, String title);

    List<Task> findByUser_IdAndStatusAndTitleContainingIgnoreCase
            (long userId, TaskStatus status, String title);

}
