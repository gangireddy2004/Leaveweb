package com.leaveweb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final ObjectProvider<JavaMailSender> sender;
    private final String from;
    public EmailService(ObjectProvider<JavaMailSender> sender, @Value("${app.mail.from:noreply@leaveweb.com}") String from) { this.sender = sender; this.from = from; }
    public void send(String recipient, String subject, String body) {
        JavaMailSender mailSender = sender.getIfAvailable();
        if (mailSender == null) { log.warn("SMTP is not configured; skipped email to {}", recipient); return; }
        try { SimpleMailMessage message = new SimpleMailMessage(); message.setFrom(from); message.setTo(recipient); message.setSubject(subject); message.setText(body); mailSender.send(message); }
        catch (RuntimeException ex) { log.warn("Unable to send email to {}: {}", recipient, ex.getMessage()); }
    }
    public void sendAccountApprovedEmail(String email) { send(email, "Leave request portal account approved", "Your LeaveWeb account has been approved. You can now log in and use the leave management portal."); }
    public void sendAccountRejectedEmail(String email, String reason) { send(email, "Leave request portal account rejected", "Your LeaveWeb account could not be approved at this time.\n\nReason: " + (reason == null ? "Not specified" : reason)); }
    public void sendLeaveApprovedEmail(String email, String details) { send(email, "Your leave request was approved", details); }
    public void sendLeaveRejectedEmail(String email, String details) { send(email, "Your leave request was rejected", details); }
    public void sendProfileUpdatedEmail(String email, String details) { send(email, "Your LeaveWeb profile details were modified", details); }
}