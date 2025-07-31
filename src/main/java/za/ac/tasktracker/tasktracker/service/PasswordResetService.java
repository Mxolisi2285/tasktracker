// src/main/java/za/ac/tasktracker/tasktracker/service/PasswordResetService.java
package za.ac.tasktracker.tasktracker.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import za.ac.tasktracker.tasktracker.model.PasswordResetToken;
import za.ac.tasktracker.tasktracker.model.User;
import za.ac.tasktracker.tasktracker.repository.PasswordResetTokenRepository;
import za.ac.tasktracker.tasktracker.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Generate token and send email
    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return; // Don't reveal if email doesn't exist (security)
        }

        // Delete old tokens
        tokenRepository.deleteByUserId(user.getId());

        // Create new token
        String tokenValue = UUID.randomUUID().toString();
        PasswordResetToken token = new PasswordResetToken(tokenValue, user);
        tokenRepository.save(token);

        sendPasswordResetEmail(user.getEmail(), tokenValue);
    }

    // Validate token and set new password
    public boolean resetPassword(String tokenValue, String newPassword) {
        var optionalToken = tokenRepository.findByToken(tokenValue);
        if (optionalToken.isEmpty()) return false;

        PasswordResetToken token = optionalToken.get();
        if (token.isExpired()) return false;

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Clean up token
        tokenRepository.delete(token);
        return true;
    }

    private void sendPasswordResetEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Password Reset Request");
            helper.setText(buildResetEmail(token), true);  // Enable HTML

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace(); // Replace with logger in production
        }
    }

    private String buildResetEmail(String token) {
        String resetUrl = "http://localhost:8080/resetPassword?token=" + token;

        Context context = new Context();
        context.setVariable("resetUrl", resetUrl);

        // ✅ Matches your file: emailPasswordReset.html
        return templateEngine.process("emailPasswordReset", context);
    }
}