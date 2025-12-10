package com.shepherd.shep_blog.services.notification;

import com.shepherd.shep_blog.data.model.TokenType;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.utils.LinkBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailNotificationServiceImpl implements MailNotificationService {
    private final MailAsyncExecutor mailAsyncExecutor;
    @Value("${client.url}")
    private String clientUrl;

    @Override
    public void sendVerificationMail(User user, String token, TokenType tokenType) {
        String verificationLink = LinkBuilder.buildWithTokenAndType(clientUrl, "/verify", token, tokenType.name());
        Map<String, Object> variables = Map.of(
                "userName", user.getUserName(),
                "confirmationLink", verificationLink
        );
        mailAsyncExecutor.sendEmailAsync("email-confirmation", "Confirm Your Email Address", user.getEmail(), variables);
    }

    @Override
    public void sendAuthorOnboardingMail(User user, String token, TokenType tokenType) {
        String verificationLink = LinkBuilder.buildWithTokenAndType(clientUrl, "/onboard", token, tokenType.name());
        Map<String, Object> variables = Map.of(
                "userName", user.getUserName(),
                "confirmationLink", verificationLink
        );
        mailAsyncExecutor.sendEmailAsync("author-onboarding", "Verify Your Author Account", user.getEmail(), variables);
    }

    @Override
    public void sendResetPasswordMail(User user, String token) {
        String resetPasswordLink = LinkBuilder.buildWithToken(clientUrl, "/reset-password", token);
        Map<String, Object> variables = Map.of(
                "firstName", user.getFirstName(),
                "resetPasswordLink", resetPasswordLink
        );
        mailAsyncExecutor.sendEmailAsync("reset-password", "Reset Your Password", user.getEmail(), variables);
    }

    @Override
    public void sendAdminInvitation(User user, String token, TokenType tokenType) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("type", tokenType.name());

        String invitationLink = LinkBuilder.build(clientUrl, "/admin-invitation", params);
        Map<String, Object> variables = Map.of(
                "firstName", user.getFirstName(),
                "invitationLink", invitationLink
        );
        mailAsyncExecutor.sendEmailAsync("admin-invitation", "Admin Invitation", user.getEmail(), variables);
    }
}


@Service
@AllArgsConstructor
@Slf4j
class MailAsyncExecutor{
    private final MailSenderService mailSenderService;
    private final SpringTemplateEngine templateEngine;

    @Async("mailTaskExecutor")
//    @Retryable(
//        value = Exception.class,
//        maxAttempts = 3,
//        backoff = @Backoff(delay = 2000)
//    )
    public void sendEmailAsync(String templateName, String subject, String email, Map<String, Object> variables) {
        try {
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(templateName, context);
            mailSenderService.sendEmail(email, subject, htmlContent);
        } catch (Exception e) {
            log.error("==>> Failed to send email [{}] to {}: {}", templateName, email, e.getMessage(), e);
            throw e;
        }
    }
}