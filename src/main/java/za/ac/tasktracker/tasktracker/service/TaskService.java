// src/main/java/za/ac/tasktracker/tasktracker/service/TaskService.java
package za.ac.tasktracker.tasktracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.tasktracker.tasktracker.model.Task;
import za.ac.tasktracker.tasktracker.model.User;
import za.ac.tasktracker.tasktracker.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> findByUser(User user) {
        return taskRepository.findByUser(user);
    }

    public void save(Task task) {
        taskRepository.save(task);
    }

    public void toggleCompleted(Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
    }

    public void updateDeadline(Long id, java.time.LocalDateTime newDeadline) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setDeadline(newDeadline);
        taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}