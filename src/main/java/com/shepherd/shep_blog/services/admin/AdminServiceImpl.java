package com.shepherd.shep_blog.services.admin;

import com.shepherd.shep_blog.data.model.Gender;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.data.model.UserRole;
import com.shepherd.shep_blog.data.repository.UserRepository;
import com.shepherd.shep_blog.services.userRoleAndPermission.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.shepherd.shep_blog.utils.RoleUtil.SUPER_ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    @Value("${superadmin_email}")
    private String superAdminEmail;
    @Value("${superadmin_password}")
    private String superAdminPassword;

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
                .isEnabled(true)
                .isEmailVerified(true)
                .build();

        userRepository.save(admin);
        log.info("Super admin created successfully");
    }
}