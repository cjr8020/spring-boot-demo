package com.example.demo.controllers;

import com.example.demo.task.Task;
import com.example.demo.task.TaskRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * In Spring's approach to building RESTful web services, HTTP requests are handled by a controller.
 * These components are easily identified by the @RestController annotation, and the @RestController
 * annotation handles GET requests for '/greeting' by returning a new instance fo the Greeting
 * class.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private TaskRepository taskRepository;

  public TaskController(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  /**
   * POST tasks.
   */
  @PostMapping
  public void addTask(@RequestBody Task task) {
    log.info("add task");
    taskRepository.save(task);
  }


  /**
   * GET tasks.
   */
  @GetMapping
  public List<Task> getTasks() {
    log.info("get tasks");
    return taskRepository.findAll();
  }


  /**
   * PUT tasks.
   */
  @PutMapping("/{id}")
  public void editTask(@PathVariable long id, @RequestBody Task task) {
    log.info("update task");
    Task existingTask = taskRepository.findOne(id);
    Assert.notNull(existingTask, "Task not found");
    existingTask.setDescription(task.getDescription());
    taskRepository.save(existingTask);
  }

  /**
   * DELETE tasks.
   */
  @DeleteMapping("/{id}")
  public void deleteTask(@PathVariable long id) {
    log.info("delete task");
    taskRepository.delete(id);
  }
}
