package com.shepherd.shep_blog.services.notification;

public interface MailSenderService {
    void sendEmail(String to, String subject, String htmlContent);
}
