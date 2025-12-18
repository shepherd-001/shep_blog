package com.shepherd.shep_blog.services.notification;

import com.shepherd.shep_blog.data.model.enums.TokenType;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.utils.LinkBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailNotificationServiceImpl implements MailNotificationService {
    private final MailAsyncExecutor mailAsyncExecutor;
    @Value("${client.url}")
    private String clientUrl;


    private String buildLink(String path, Map<String, String> params) {
        return LinkBuilder.build(clientUrl, path, params);
    }

    @Override
    public void sendVerificationMail(User user, String token, TokenType tokenType) {
        String verificationLink = buildLink("/verify", Map.of(
                "token", token,
                "type", tokenType.name()
        ));

        EmailRequest emailRequest = EmailRequest.builder()
                .template(EmailTemplate.EMAIL_CONFIRMATION)
                .subject("Confirm Your Email Address")
                .recipientEmail(user.getEmail())
                .variables(Map.of(
                        "displayName", user.getUserName(),
                        "confirmationLink", verificationLink))
                .build();
        mailAsyncExecutor.sendEmailAsync(emailRequest);
    }

    @Override
    public void sendAuthorOnboardingMail(User user, String token, TokenType tokenType) {
        String verificationLink = buildLink("/onboard", Map.of(
                "token", token,
                "type", tokenType.name()
        ));

        EmailRequest emailRequest = EmailRequest.builder()
                .template(EmailTemplate.AUTHOR_ONBOARDING)
                .subject("Verify Your Author Account")
                .recipientEmail(user.getEmail())
                .variables(Map.of(
                        "displayName", user.getUserName(),
                        "confirmationLink", verificationLink))
                .build();
        mailAsyncExecutor.sendEmailAsync(emailRequest);
    }

    @Override
    public void sendResetPasswordMail(User user, String token) {
        String resetPasswordLink = buildLink("/reset-password", Map.of("token", token));

        EmailRequest emailRequest = EmailRequest.builder()
                .template(EmailTemplate.RESET_PASSWORD)
                .subject("Reset Your Password")
                .recipientEmail(user.getEmail())
                .variables(Map.of(
                        "displayName", user.getFirstName(),
                        "resetPasswordLink", resetPasswordLink))
                .build();
        mailAsyncExecutor.sendEmailAsync(emailRequest);
    }

    @Override
    public void sendAdminInvitation(User user, String token, TokenType tokenType) {
        String link = buildLink("/invitation", Map.of(
                "token", token,
                "type", tokenType.name()));

        EmailRequest emailRequest = EmailRequest.builder()
                .template(EmailTemplate.ADMIN_INVITATION)
                .subject("Invitation to Shep Blog")
                .recipientEmail(user.getEmail())
                .variables(Map.of(
                        "invitationLink", link))
                .build();
        mailAsyncExecutor.sendEmailAsync(emailRequest);
    }
}

@Getter
enum EmailTemplate {
    EMAIL_CONFIRMATION("email-confirmation"),
    AUTHOR_ONBOARDING("author-onboarding"),
    RESET_PASSWORD("reset-password"),
    ADMIN_INVITATION("admin-invitation");

    private final String templateName;

    EmailTemplate(String templateName) {
        this.templateName = templateName;
    }
}

@Builder
@Getter
class EmailRequest {
    private final EmailTemplate template;
    private final String subject;
    private final String recipientEmail;
    private final Map<String, Object> variables;
}

@Service
@AllArgsConstructor
@Slf4j
class MailAsyncExecutor{
    private final MailSenderService mailSenderService;
    private final SpringTemplateEngine templateEngine;

    @Async("mailTaskExecutor")
    public void sendEmailAsync(EmailRequest emailRequest) {
        String email = emailRequest.getRecipientEmail();
        String template = emailRequest.getTemplate().getTemplateName();

        try {
            Context context = new Context();
            context.setVariables(emailRequest.getVariables());
            String htmlContent = templateEngine.process(template, context);
            mailSenderService.sendEmail(email, emailRequest.getSubject(), htmlContent);
        } catch (Exception e) {
            log.error("==>> Failed to send email [{}] to {}: {}", template, email, e.getMessage(), e);
            throw e;
        }
    }
}