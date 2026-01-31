package com.shepherd.shep_blog.services.notification;

import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.data.model.enums.TokenType;

public interface MailNotificationService {
    void sendVerificationMail(User user, String token, TokenType tokenType);
    void sendAuthorOnboardingMail(User user, String token, TokenType tokenType);
    void sendResetPasswordMail(User user, String token);
    void sendAdminInvitation(User user, String token,  TokenType tokenType);
    void sendAuthorMemberInvitation(User user, String token, String inviterName, String role, TokenType tokenType);
}
