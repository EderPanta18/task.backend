package com.example.taskmanager.controllers;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.taskmanager.helps.UUIDConverter;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.services.TaskService;
import com.example.taskmanager.services.UserService;

@RestController
@RequestMapping(path = "/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        UUID uuid = UUIDConverter.checkStringToUUID(id);
        if (uuid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task task = taskService.getTaskById(uuid);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUserId(@PathVariable String userId) {
        UUID uuid = UUIDConverter.checkStringToUUID(userId);
        if (uuid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Task> tasks = taskService.getTaskByUserId(uuid);
        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = task.getUser();
        if (user != null) {
            User existingUser = userService.getUserdById(user.getId());
            if (existingUser != null) {
                task.setUser(existingUser);
                Task createdTask = taskService.createTask(task);
                existingUser.addTaskToList(createdTask);
                userService.updateUser(existingUser.getId(), existingUser);
                return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/random/{userId}")
    public ResponseEntity<Task> createRandomTaskForUser(@PathVariable String userId) {
        UUID uuid = UUIDConverter.checkStringToUUID(userId);
        if (uuid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getUserdById(uuid);
        if (user != null) {
            Task randomTask = new Task();
            randomTask.setTitle("Titulo" + new Random().nextInt(1000));
            randomTask.setDescription("Descripcion" + new Random().nextInt(1000));
            randomTask.setCompleted(new Random().nextBoolean());
            randomTask.setUser(user);
            Task createdTask = taskService.createTask(randomTask);
            user.addTaskToList(createdTask);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task taskDetails) {
        UUID taskId = UUIDConverter.checkStringToUUID(id);
        if (taskId == null || taskDetails == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task updatedTask = taskService.updateTask(taskId, taskDetails);
        if (updatedTask != null) {
            User user = updatedTask.getUser();
            if (user != null) {
                user.updateTaskToList(taskId, updatedTask);
                userService.updateUser(user.getId(), user);
            }
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        UUID taskId = UUIDConverter.checkStringToUUID(id);
        if (taskId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task taskToDelete = taskService.getTaskById(taskId);
        if (taskToDelete != null) {
            taskService.deleteTask(taskId);
            User user = taskToDelete.getUser();
            if (user != null) {
                user.removeTaskToList(taskId);
                userService.updateUser(user.getId(), user);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
