package com.shepherd.shep_blog.services.user;

import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterReaderRequest;
import com.shepherd.shep_blog.data.dto.response.AuthorResponse;
import com.shepherd.shep_blog.data.dto.response.RegisterUserResponse;
import com.shepherd.shep_blog.data.model.*;
import com.shepherd.shep_blog.data.repository.AuthorRepository;
import com.shepherd.shep_blog.data.repository.ReaderRepository;
import com.shepherd.shep_blog.data.repository.UserRepository;
import com.shepherd.shep_blog.exceptions.AlreadyExistsException;
import com.shepherd.shep_blog.mapper.UserMapper;
import com.shepherd.shep_blog.services.notification.MailNotificationService;
import com.shepherd.shep_blog.services.token.TokenService;
import com.shepherd.shep_blog.services.userRoleAndPermission.RoleService;
import com.shepherd.shep_blog.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.shepherd.shep_blog.utils.ErrorMessage.*;
import static com.shepherd.shep_blog.utils.RoleUtil.*;

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
    private final AuthorRepository authorRepository;


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
    @Transactional
    public AuthorResponse registerAuthor(RegisterAuthorRequest request) {
        checkIfUserEmailExists(request.getEmail());
        checkIfUserNameExists(request.getUserName());
        checkIfWebsiteAddressIsValid(request.getWebsiteAddress());

        User user = userMapper.mapToUser(request);
        UserRole role = roleService.getRole(SUPER_AUTHOR);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Member member = Member.builder()
                .user(user)
                .build();

        Author author = Author.builder()
                .organizationPhoneNumber(request.getOrganizationPhoneNumber().trim())
                .websiteAddress(request.getWebsiteAddress().trim().toLowerCase())
                .build();
        author.addMember(member);
        author = authorRepository.save(author);

        TokenType tokenType = TokenType.AUTHOR_SIGN_UP;
        String token = tokenService.generateToken(user.getEmail(), tokenType);
        notificationService.sendAuthorOnboardingMail(user, token, tokenType);
        return buildAuthorResponse(author);
    }


    private void checkIfUserEmailExists(String email) {
        if(userRepository.existsByEmailEqualsIgnoreCase(email.trim())) {
            throw new AlreadyExistsException(USER_EMAIL_ALREADY_EXISTS);
        }
    }

    private void checkIfUserNameExists(String username) {
        if(userRepository.existsByUserNameEqualsIgnoreCase(username.trim())) {
            throw new AlreadyExistsException(USER_NAME_ALREADY_EXISTS);
        }
    }

    private void checkIfWebsiteAddressIsValid(String websiteAddress) {
        if(!AppUtils.isValidUri(websiteAddress))
            throw new IllegalArgumentException(INVALID_WEBSITE_ADDRESS);
    }

    private AuthorResponse buildAuthorResponse(Author author) {
        List<Member> members = author.getMembers();
        return AuthorResponse.builder()
                .members(members.stream()
                        .map(member -> userMapper.mapToUserResponse(member.getUser()))
                        .toList())
                .organizationPhoneNumber(author.getOrganizationPhoneNumber())
                .websiteAddress(author.getWebsiteAddress())
                .build();
    }
}