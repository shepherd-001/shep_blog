package com.shepherd.shep_blog.services.admin;

import com.shepherd.shep_blog.data.dto.request.InviteAdminRequest;
import com.shepherd.shep_blog.data.model.*;
import com.shepherd.shep_blog.data.repository.AdminRepository;
import com.shepherd.shep_blog.data.repository.UserRepository;
import com.shepherd.shep_blog.exceptions.AlreadyExistsException;
import com.shepherd.shep_blog.services.notification.MailNotificationService;
import com.shepherd.shep_blog.services.token.TokenService;
import com.shepherd.shep_blog.services.userRoleAndPermission.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.shepherd.shep_blog.utils.ErrorMessage.USER_EMAIL_ALREADY_EXISTS;
import static com.shepherd.shep_blog.utils.RoleUtil.ADMIN;
import static com.shepherd.shep_blog.utils.RoleUtil.SUPER_ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final AdminRepository  adminRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    @Value("${superadmin_email}")
    private String superAdminEmail;
    @Value("${superadmin_password}")
    private String superAdminPassword;
    private final TokenService tokenService;
    private final MailNotificationService mailNotificationService;

//    @PostConstruct
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
//            user = userRepository.save(user);

            Admin admin = Admin.builder()
                    .user(user)
                    .build();

            adminRepository.save(admin);

            TokenType tokenType = TokenType.ADMIN_INVITATION;
            String token = tokenService.generateToken(user.getEmail(), tokenType);
            mailNotificationService.sendAdminInvitation(user, token, tokenType);
        }
        return "Admins invited successfully";
    }
}