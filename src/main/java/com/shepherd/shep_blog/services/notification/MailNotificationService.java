package com.shepherd.shep_blog.services.notification;

import com.shepherd.shep_blog.data.model.User;

public interface MailNotificationService {
    void sendVerificationMail(User user, String token);
    void sendResetPasswordMail(User user, String token);
    void sendAdminInvitation(User user, String token);
}
