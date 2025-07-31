// src/main/java/za/ac/tasktracker/tasktracker/TasktrackerApplication.java
package za.ac.tasktracker.tasktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "za.ac.tasktracker.tasktracker")
@EnableScheduling
public class TasktrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasktrackerApplication.class, args);
	}
}