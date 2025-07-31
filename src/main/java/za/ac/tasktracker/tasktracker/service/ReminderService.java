// src/main/java/za/ac/tasktracker/tasktracker/service/ReminderService.java

package za.ac.tasktracker.tasktracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import za.ac.tasktracker.tasktracker.model.Task;
import za.ac.tasktracker.tasktracker.model.User;
import za.ac.tasktracker.tasktracker.repository.TaskRepository;

import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    // Run every hour
    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void sendDeadlineReminders() {
        LocalDateTime now = LocalDateTime.now();

        // === 1. Send 24-hour advance reminders ===
        LocalDateTime start24h = now.plusHours(23);   // From 23 hours from now
        LocalDateTime end24h = now.plusHours(25);     // Up to 25 hours (buffer)

        List<Task> upcomingTasks = taskRepository.findByDeadlineBetweenAndCompletedFalse(start24h, end24h);
        for (Task task : upcomingTasks) {
            sendReminderEmail(task.getUser().getEmail(), task.getUser().getUsername(), task);
        }

        // === 2. Send urgent (1-hour) reminders ===
        LocalDateTime startUrgent = now.minusMinutes(5);  // Allow small past buffer
        LocalDateTime endUrgent = now.plusMinutes(65);    // Up to 60 minutes from now

        List<Task> urgentTasks = taskRepository.findByDeadlineBetweenAndCompletedFalse(startUrgent, endUrgent);
        for (Task task : urgentTasks) {
            sendUrgentReminder(task.getUser().getEmail(), task.getUser().getUsername(), task);
        }
    }

    // Standard reminder (e.g., 24 hours before)
    private void sendReminderEmail(String to, String username, Task task) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("📅 Reminder: Task Due Soon – " + task.getTitle());

            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("taskTitle", task.getTitle());
            context.setVariable("taskPriority", task.getPriority());
            context.setVariable("deadline", task.getDeadline());

            String htmlBody = templateEngine.process("email/task-reminder", context);
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Urgent reminder (within 1 hour)
    private void sendUrgentReminder(String to, String username, Task task) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("⏰ URGENT: Task Due in 1 Hour! – " + task.getTitle());

            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("taskTitle", task.getTitle());
            context.setVariable("taskPriority", task.getPriority());
            context.setVariable("deadline", task.getDeadline());

            String htmlBody = templateEngine.process("email/task-urgent-reminder", context);
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}