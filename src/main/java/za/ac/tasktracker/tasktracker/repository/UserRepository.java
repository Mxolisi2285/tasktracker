// src/main/java/za/ac/tasktracker/tasktracker/repository/UserRepository.java
package za.ac.tasktracker.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.tasktracker.tasktracker.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}