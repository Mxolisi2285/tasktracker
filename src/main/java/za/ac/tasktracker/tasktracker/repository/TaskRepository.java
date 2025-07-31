// src/main/java/za/ac/tasktracker/tasktracker/repository/TaskRepository.java
package za.ac.tasktracker.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.tasktracker.tasktracker.model.Task;
import za.ac.tasktracker.tasktracker.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);

    // Find tasks due between two times and not completed
    List<Task> findByDeadlineBetweenAndCompletedFalse(LocalDateTime start, LocalDateTime end);
}