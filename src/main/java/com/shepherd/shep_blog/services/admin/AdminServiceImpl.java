package com.shepherd.shep_blog.services.admin;

import com.shepherd.shep_blog.data.model.Gender;
import com.shepherd.shep_blog.data.model.Role;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.data.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_PASSWORD = "Admin123$";

    @PostConstruct
    private void createAdminIfNotExists() {
        if(userRepository.existsByRole(Role.ADMIN)){
            log.info("Admin already exists");
            return;
        }
        User admin = User.builder()
                .firstName("Admin")
                .lastName("Admin")
                .gender(Gender.MALE)
                .email("admin@mailinator.com")
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .role(Role.ADMIN)
                .isEnabled(true)
                .isEmailVerified(true)
                .build();

        userRepository.save(admin);
        log.info("Admin created successfully");
    }
}