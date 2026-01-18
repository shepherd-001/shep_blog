package com.shepherd.shep_blog.services.user;

import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterReaderRequest;
import com.shepherd.shep_blog.data.dto.response.PaginationResponse;
import com.shepherd.shep_blog.data.dto.response.RegisterUserResponse;
import com.shepherd.shep_blog.data.dto.response.UserResponse;
import com.shepherd.shep_blog.data.model.Reader;
import com.shepherd.shep_blog.data.model.enums.TokenType;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.data.model.UserRole;
import com.shepherd.shep_blog.data.repository.ReaderRepository;
import com.shepherd.shep_blog.data.repository.UserRepository;
import com.shepherd.shep_blog.exceptions.AlreadyExistsException;
import com.shepherd.shep_blog.mapper.UserMapper;
import com.shepherd.shep_blog.services.notification.MailNotificationService;
import com.shepherd.shep_blog.services.token.TokenService;
import com.shepherd.shep_blog.services.userRoleAndPermission.RoleService;
import com.shepherd.shep_blog.utils.RoleUtil;
import com.shepherd.shep_blog.utils.pagination_utils.PageMapper;
import com.shepherd.shep_blog.utils.pagination_utils.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.shepherd.shep_blog.utils.ErrorMessage.USER_EMAIL_ALREADY_EXISTS;
import static com.shepherd.shep_blog.utils.ErrorMessage.USER_NAME_ALREADY_EXISTS;
import static com.shepherd.shep_blog.utils.RoleUtil.READER;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ReaderRepository readerRepository;
    private final MailNotificationService notificationService;
    private final UserMapper userMapper;
    private final TokenService tokenService;
    private final RoleService roleService;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("firstName", "lastName",
            "userName", "email", "gender", "createdAt");


    @Override
    @Transactional
    public RegisterUserResponse registerReader(RegisterReaderRequest request) {
        checkIfUserEmailExists(request.getEmail());
        checkIfUserNameExists(request.getUserName());

        User user = userMapper.mapToUser(request);
        UserRole role = roleService.getRole(READER);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Reader reader = Reader.builder()
                .user(user)
                .build();
        readerRepository.save(reader);

        TokenType tokenType = TokenType.EMAIL_CONFIRMATION;
        String token = tokenService.generateToken(user.getEmail(), tokenType);
        notificationService.sendVerificationMail(user, token, tokenType);
        return userMapper.mapToRegisterReaderResponse(user);
    }

    @Override
    public void checkIfUserEmailExists(String email) {
        if(userRepository.existsByEmailIgnoreCase(email.trim())) {
            throw new AlreadyExistsException(USER_EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    public void checkIfUserNameExists(String username) {
        if(userRepository.existsByUsernameIgnoreCase(username.trim())) {
            throw new AlreadyExistsException(USER_NAME_ALREADY_EXISTS);
        }
    }

    @Override
    public PaginationResponse<UserResponse> getAllEnabledUser(boolean enabled, PaginationRequest request) {
        Pageable pageable = PageRequestFactory.create(request, ALLOWED_SORT_FIELDS);
        Page<User> users = userRepository.findAllEnabledExcludingRole(enabled, RoleUtil.SUPER_ADMIN, pageable);
        log.info("==>> Fetching all user: enabled -> '{}'", enabled);
        return PageMapper.map(users, userMapper::mapToUserResponse);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}