package com.skillforge.backend.config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Configuration
public class EmailConfig {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String toEmail, String password, String username) {
        try {
            String subject = "Welcome TO Skillforge";
            String htmlContent = getHtmlContent(username, password);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(htmlContent, true);
            mimeMessageHelper.setFrom(fromEmail);

            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getHtmlContent(String username, String password) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Welcome to SkillForge</title>" +
                "</head>" +
                "<body>" +
                "<h2>Congratulations!</h2>" +
                "<p>Admin has successfully registered your account to SkillForge.</p>" +
                "<p>To access your portal, log in using the following credentials:</p>" +
                "<ul>" +
                "<li><strong>Username:</strong> " + username + "</li>" +
                "<li><strong>Password:</strong> " + password + "</li>" +
                "</ul>" +
                "<p>We’re excited to have you with us!</p>" +
                "</body>" +
                "</html>";
    }
}
