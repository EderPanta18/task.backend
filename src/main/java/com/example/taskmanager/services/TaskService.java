package com.example.taskmanager.services;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repositorys.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        return task == null ? null : taskRepository.save(task);
    }

    public Task updateTask(UUID id, Task taskDetails) {
        Task task = getTaskById(id);
        if (task != null && taskDetails != null) {
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setCompleted(taskDetails.isCompleted());
            return taskRepository.save(task);
        }
        return null;
    }

    public void deleteTask(UUID id) {
        Task task = getTaskById(id);
        if (task == null) {
            return;
        }
        taskRepository.deleteById(id);
    }

    public List<Task> getTaskByUserId(UUID userId) {
        return taskRepository.findByUserId(userId);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(UUID id) {
        return taskRepository.findById(id).orElse(null);
    }
}
