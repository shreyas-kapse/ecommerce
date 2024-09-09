package com.e_commerce.backend;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void loginMail(String to, String subject, String body, String from, String name) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setFrom(from);
            message.setSubject(subject);
            String htmlTemplate = readFile("src/main/resources/mailTemplate/Login.html");
            String htmlContent = htmlTemplate.replace("${name}", name);

            message.setContent(htmlContent, "text/html; charset=utf-8");
            javaMailSender.send(message);
        } catch (Exception e) {
            System.out.println("Exception occurred while sending mail.");
        }
    }

    public String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}