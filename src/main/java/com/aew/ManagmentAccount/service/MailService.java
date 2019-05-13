package com.aew.ManagmentAccount.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import com.aew.ManagmentAccount.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String BASE_URL = "http://localhost:8080/api/v1/user/activation/";

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String content) {

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom("noreply@NameOfApp");
            message.setSubject(subject);
            message.setText(content, true);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmail(user.getEmail(), "mail/activationEmail", BASE_URL + user.getActivationKey());
    }

    public void sendPasswordMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmail(user.getEmail(), "mail/passwordResetEmail", "Su password es: " + user.getPassword());
    }

}