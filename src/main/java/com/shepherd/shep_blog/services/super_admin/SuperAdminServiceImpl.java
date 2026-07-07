package com.shepherd.shep_blog.services.super_admin;

import com.shepherd.shep_blog.data.dto.request.InviteAdminRequest;
import com.shepherd.shep_blog.data.dto.response.InvitationResponse;
import com.shepherd.shep_blog.data.model.Invitation;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.data.model.UserRole;
import com.shepherd.shep_blog.data.model.enums.Gender;
import com.shepherd.shep_blog.data.model.enums.InvitationStatus;
import com.shepherd.shep_blog.data.model.enums.TokenType;
import com.shepherd.shep_blog.data.repository.InvitationRepository;
import com.shepherd.shep_blog.data.repository.UserRepository;
import com.shepherd.shep_blog.common.exceptions.AlreadyExistsException;
import com.shepherd.shep_blog.common.exceptions.ResourceNotFoundException;
import com.shepherd.shep_blog.common.exceptions.ShepBlogException;
import com.shepherd.shep_blog.services.notification.MailNotificationService;
import com.shepherd.shep_blog.services.token.TokenService;
import com.shepherd.shep_blog.services.userRoleAndPermission.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.shepherd.shep_blog.utils.ErrorMessage.INVITATION_NOT_FOUND;
import static com.shepherd.shep_blog.utils.ErrorMessage.USER_EMAIL_ALREADY_EXISTS;
import static com.shepherd.shep_blog.utils.RoleUtil.ADMIN;
import static com.shepherd.shep_blog.utils.RoleUtil.SUPER_ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class SuperAdminServiceImpl implements SuperAdminService {
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    @Value("${superadmin_email}")
    private String superAdminEmail;
    @Value("${superadmin_password}")
    private String superAdminPassword;
    private final TokenService tokenService;
    private final MailNotificationService mailNotificationService;

    public void createSuperAdminIfNotExists() {
        if(userRepository.existsByRoles_Name(SUPER_ADMIN)){
            log.info("==>> Super admin already exists");
            return;
        }
        UserRole role = roleService.getRole(SUPER_ADMIN);
        User admin = User.builder()
                .firstName("Shep")
                .lastName("Admin")
                .gender(Gender.MALE)
                .email(superAdminEmail)
                .password(passwordEncoder.encode(superAdminPassword))
                .roles(Set.of(role))
                .enabled(false)
                .emailVerified(false)
                .build();

        userRepository.save(admin);
        log.info("==>> Super admin created successfully");

        TokenType tokenType = TokenType.ADMIN_INVITATION;
        String token = tokenService.generateToken(admin.getEmail(), tokenType);
        mailNotificationService.sendAdminInvitation(admin, token, tokenType);
    }

    @Override
    @Transactional
    public List<InvitationResponse> inviteAdmin(InviteAdminRequest inviteAdminRequest) {
        List<InvitationResponse> invitationResponses = new ArrayList<>();
        for(String email : inviteAdminRequest.getAdminEmails()){
            email = email.trim();
            if(userRepository.existsByEmailIgnoreCase(email)){
                throw new AlreadyExistsException(USER_EMAIL_ALREADY_EXISTS);
            }

            UserRole role = roleService.getRole(ADMIN);
            User user = User.builder()
                    .email(email.toLowerCase())
                    .roles(Set.of(role))
                    .build();

            Invitation invitation = Invitation.builder()
                    .status(InvitationStatus.PENDING)
                    .user(user)
                    .build();

            invitation = invitationRepository.save(invitation);

            invitationResponses.add(InvitationResponse.builder()
                            .invitationId(invitation.getId())
                            .status(InvitationStatus.PENDING)
                    .build());

            TokenType tokenType = TokenType.ADMIN_INVITATION;
            String token = tokenService.generateToken(user.getEmail(), tokenType);
            mailNotificationService.sendAdminInvitation(user, token, tokenType);
        }

        log.info("==>> {} admins invited",  invitationResponses.size());
        return invitationResponses;
    }

    @Override
    public InvitationResponse cancelInvitation(UUID inviteId) {
        Invitation invitation = invitationRepository.findById(inviteId)
                .orElseThrow(()-> new ResourceNotFoundException(INVITATION_NOT_FOUND));

        if(!invitation.getStatus().equals(InvitationStatus.PENDING)){
            throw new ShepBlogException("Invitation cannot be cancelled");
        }
        
        invitationRepository.delete(invitation);

        log.info("==>> Invitation '{}' cancelled successfully", inviteId);
        return InvitationResponse.builder()
                .status(InvitationStatus.CANCELLED)
                .build();
    }
}