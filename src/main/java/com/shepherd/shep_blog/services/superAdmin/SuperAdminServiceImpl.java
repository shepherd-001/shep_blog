package com.shepherd.shep_blog.services.superAdmin;

import com.shepherd.shep_blog.data.dto.request.InviteAdminRequest;
import com.shepherd.shep_blog.data.dto.response.InvitationResponse;
import com.shepherd.shep_blog.data.model.*;
import com.shepherd.shep_blog.data.repository.AdminRepository;
import com.shepherd.shep_blog.data.repository.InvitationRepository;
import com.shepherd.shep_blog.data.repository.UserRepository;
import com.shepherd.shep_blog.exceptions.AlreadyExistsException;
import com.shepherd.shep_blog.exceptions.ResourceNotFoundException;
import com.shepherd.shep_blog.exceptions.ShepBlogException;
import com.shepherd.shep_blog.services.notification.MailNotificationService;
import com.shepherd.shep_blog.services.token.TokenService;
import com.shepherd.shep_blog.services.userRoleAndPermission.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.shepherd.shep_blog.utils.ErrorMessage.INVITATION_NOT_FOUND;
import static com.shepherd.shep_blog.utils.ErrorMessage.USER_EMAIL_ALREADY_EXISTS;
import static com.shepherd.shep_blog.utils.RoleUtil.ADMIN;
import static com.shepherd.shep_blog.utils.RoleUtil.SUPER_ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class SuperAdminServiceImpl implements SuperAdminService {
    private final UserRepository userRepository;
    private final AdminRepository  adminRepository;
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
        if(userRepository.existsByRoleName(SUPER_ADMIN)){
            log.info("Super admin already exists");
            return;
        }
        UserRole role = roleService.getRole(SUPER_ADMIN);
        User admin = User.builder()
                .firstName("Shep")
                .lastName("Admin")
                .gender(Gender.MALE)
                .email(superAdminEmail)
                .password(passwordEncoder.encode(superAdminPassword))
                .role(role)
                .enabled(true)
                .emailVerified(true)
                .build();

        userRepository.save(admin);
        log.info("Super admin created successfully");
    }

    @Override
//    @Transactional
    public String inviteAdmin(InviteAdminRequest inviteAdminRequest) {
        for(String email : inviteAdminRequest.getAdminEmails()){
            email = email.trim();
            if(userRepository.existsByEmailEqualsIgnoreCase(email)){
                throw new AlreadyExistsException(USER_EMAIL_ALREADY_EXISTS);
            }

            UserRole role = roleService.getRole(ADMIN);
            User user = User.builder()
                    .email(email.toLowerCase())
                    .role(role)
                    .build();

            Invitation invitation = Invitation.builder()
                    .status(InvitationStatus.PENDING)
                    .user(user)
                    .build();

            invitationRepository.save(invitation);

            TokenType tokenType = TokenType.ADMIN_INVITATION;
            String token = tokenService.generateToken(user.getEmail(), tokenType);
            mailNotificationService.sendAdminInvitation(user, token, tokenType);
        }
        return "Admins invited successfully";
    }

    @Override
    public InvitationResponse cancelInvitation(String inviteId) {
        Invitation invitation = invitationRepository.findById(inviteId)
                .orElseThrow(()-> new ResourceNotFoundException(INVITATION_NOT_FOUND));

        if(!invitation.getStatus().equals(InvitationStatus.PENDING)){
            throw new ShepBlogException("Invitation can not be cancelled");
        }
        invitation.setStatus(InvitationStatus.CANCELLED);
        invitation.setRespondedAt(Instant.now());
        invitation = invitationRepository.save(invitation);

        log.info("Invitation cancelled successfully");
        return InvitationResponse.builder()
                .status(invitation.getStatus())
                .build();
    }
}

//@Modifying
//@Query("""
//    UPDATE Invitation i
//    SET i.status = :newStatus
//    WHERE i.id = :id AND i.status = :requiredStatus
//""")
//int updateStatus(
//    @Param("id") String id,
//    @Param("requiredStatus") InvitationStatus requiredStatus,
//    @Param("newStatus") InvitationStatus newStatus
//);

//@Override
//@Transactional
//public InvitationResponse cancelInvitation(String invitationId) {
//    int updated = invitationRepository.updateStatus(
//            invitationId,
//            InvitationStatus.PENDING,
//            InvitationStatus.CANCELLED
//    );
//
//    if (updated == 0) {
//        throw new ShepBlogException("Invitation cannot be cancelled");
//    }
//
//    return InvitationResponse.builder()
//            .status(InvitationStatus.CANCELLED)
//            .build();
//}