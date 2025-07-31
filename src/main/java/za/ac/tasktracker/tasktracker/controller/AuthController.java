// src/main/java/za/ac/tasktracker/tasktracker/controller/AuthController.java
package za.ac.tasktracker.tasktracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import za.ac.tasktracker.tasktracker.model.Task;
import za.ac.tasktracker.tasktracker.model.User;
import za.ac.tasktracker.tasktracker.service.PasswordResetService;
import za.ac.tasktracker.tasktracker.service.TaskService;
import za.ac.tasktracker.tasktracker.service.UserService;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class AuthController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetService passwordResetService;

    // ================================
    // Dashboard
    // ================================
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            model.addAttribute("tasks", taskService.findByUser(user));
        }
        return "dashboard";
    }

    // ================================
    // Task Management
    // ================================
    @PostMapping("/tasks")
    public String createTask(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam Task.Priority priority,
            @RequestParam(required = false) LocalDateTime deadline,
            Principal principal,
            Model model) {

        User user = userService.findByUsername(principal.getName());

        if (deadline == null) {
            deadline = LocalDateTime.now().plusDays(7); // Default deadline
        }

        Task task = new Task(title, description, priority, deadline, user);
        taskService.save(task);

        return "redirect:/dashboard?added";
    }

    @PostMapping("/tasks/toggle")
    public String toggleTask(@RequestParam Long taskId) {
        taskService.toggleCompleted(taskId);
        return "redirect:/dashboard";
    }

    @PostMapping("/tasks/updateDeadline")
    public String updateDeadline(@RequestParam Long taskId, @RequestParam LocalDateTime newDeadline) {
        taskService.updateDeadline(taskId, newDeadline);
        return "redirect:/dashboard";
    }

    @PostMapping("/tasks/delete")
    public String deleteTask(@RequestParam Long taskId) {
        taskService.delete(taskId);
        return "redirect:/dashboard?deleted";
    }

    // ================================
    // Authentication: Register
    // ================================
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        if (password == null || password.trim().length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters.");
            return "redirect:/register";
        }

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/register";
        }

        if (userService.findByUsername(username) != null) {
            redirectAttributes.addFlashAttribute("error", "Username already taken.");
            return "redirect:/register";
        }

        if (userService.findByEmail(email) != null) {
            redirectAttributes.addFlashAttribute("error", "Email already registered.");
            return "redirect:/register";
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password); // ⚠️ Ensure hashing is done in UserService!

        userService.save(user);

        redirectAttributes.addAttribute("success", true);
        return "redirect:/login";
    }

    // ================================
    // Authentication: Login
    // ================================
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // ================================
    // Forgot Password
    // ================================
    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String sendResetLink(
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {

        passwordResetService.createPasswordResetToken(email);
        redirectAttributes.addAttribute("success", true);
        return "redirect:/forgotPassword";
    }

    // ================================
    // Reset Password
    // ================================
    @GetMapping("/resetPassword")
    public String showResetForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(
            @RequestParam String token,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("token", token);
            return "resetPassword";
        }

        boolean success = passwordResetService.resetPassword(token, password);
        if (success) {
            model.addAttribute("success", true);
            return "login";
        } else {
            model.addAttribute("error", "Invalid or expired reset link.");
            return "forgotPassword";
        }
    }


}