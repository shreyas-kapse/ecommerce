package com.e_commerce.backend;

import com.e_commerce.backend.enity.OrderItem;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void loginMail(String to, String subject, String body, String from, String name) {
        try {
            log.info("Sending login mail to {}", to);
            MimeMessage message = javaMailSender.createMimeMessage();

            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setFrom(from);
            message.setSubject(subject);
            String htmlTemplate = readFile("src/main/resources/mailTemplate/Login.html");
            String htmlContent = htmlTemplate.replace("${name}", name);

            message.setContent(htmlContent, "text/html; charset=utf-8");
            javaMailSender.send(message);
            log.info("Successfully sent mail to {}", to);
        } catch (Exception e) {
            System.out.println("Exception occurred while sending mail.");
            log.error("Error occurred while sending mail to {} and error {}", to, e.getMessage());
        }
    }

    @Async
    public void merchantAccountCreationMail(String to, String subject, String from, String name, String username, String password) {
        try {
            log.info("Sending login mail to {}", to);
            MimeMessage message = javaMailSender.createMimeMessage();

            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setFrom(from);
            message.setSubject(subject);
            String htmlTemplate = readFile("src/main/resources/mailTemplate/MerchantAccountCreation.html");
            String htmlContent = htmlTemplate.replace("${merchantName}", name)
                    .replace("${username}", username)
                    .replace("${password}", password);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            javaMailSender.send(message);
            log.info("Successfully sent mail to {}", to);
        } catch (Exception e) {
            log.error("Error occurred while sending mail to {} and error {}", to, e.getMessage());
        }
    }

    @Async
    public void orderConfirmationMail(String to, String subject, String from, String customerName, String orderNumber, String orderDate, List<OrderItem> orderItems, String shippingAddress, String deliveryDate) {
        try {
            log.info("Sending login mail to {}", to);
            MimeMessage message = javaMailSender.createMimeMessage();

            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setFrom(from);
            message.setSubject(subject);
            String htmlTemplate = readFile("src/main/resources/mailTemplate/OrderConfirmation.html");

            StringBuilder orderedItemsHtml = new StringBuilder();

            for (OrderItem item : orderItems) {
                orderedItemsHtml.append("<tr>")
                        .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(item.getProduct().getProductName()).append("</td>")
                        .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(item.getQuantity()).append("</td>")
                        .append("<td style='border: 1px solid #ddd; padding: 8px;'>$").append(item.getPrice()).append("</td>")
                        .append("</tr>");
            }
            String htmlContent = htmlTemplate.replace("${customerName}", customerName)
                    .replace("${orderNumber}", orderNumber)
                    .replace("${orderDate}", orderDate)
                    .replace("${orderedItems}", orderedItemsHtml.toString())
                    .replace("${shippingAddress}", shippingAddress)
                    .replace("${deliveryDate}", deliveryDate);

            message.setContent(htmlContent, "text/html; charset=utf-8");
            javaMailSender.send(message);
            log.info("Successfully sent mail to {}", to);
        } catch (Exception e) {
            log.error("Error occurred while sending mail to {} and error {}", to, e.getMessage());
        }
    }

    public String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}