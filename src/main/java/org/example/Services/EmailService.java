package org.example.Services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${email.to}")
    private String toEmail;

    @Async
    public void sendEmailAsync(String subject, String body, String replyTo) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        helper.setText(body, false);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setFrom(fromEmail);
        helper.setReplyTo(replyTo);

        mailSender.send(message);
    }
}
