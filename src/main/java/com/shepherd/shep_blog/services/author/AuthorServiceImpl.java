package com.shepherd.shep_blog.services.author;

import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.dto.response.AuthorResponse;
import com.shepherd.shep_blog.data.dto.response.PaginationResponse;
import com.shepherd.shep_blog.data.model.*;
import com.shepherd.shep_blog.data.model.enums.TokenType;
import com.shepherd.shep_blog.data.repository.AuthorRepository;
import com.shepherd.shep_blog.mapper.UserMapper;
import com.shepherd.shep_blog.services.notification.MailNotificationService;
import com.shepherd.shep_blog.services.token.TokenService;
import com.shepherd.shep_blog.services.user.UserService;
import com.shepherd.shep_blog.services.userRoleAndPermission.RoleService;
import com.shepherd.shep_blog.utils.AppUtils;
import com.shepherd.shep_blog.utils.pagination_utils.PageMapper;
import com.shepherd.shep_blog.utils.pagination_utils.PageRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.shepherd.shep_blog.utils.ErrorMessage.INVALID_WEBSITE_ADDRESS;
import static com.shepherd.shep_blog.utils.RoleUtil.SUPER_AUTHOR;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TokenService tokenService;
    private final MailNotificationService notificationService;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("organizationPhoneNumber", "createdAt");

    @Override
    public AuthorResponse registerAuthor(RegisterAuthorRequest request) {
        userService.checkIfUserEmailExists(request.getEmail());
        userService.checkIfUserNameExists(request.getUserName());
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

    private void checkIfWebsiteAddressIsValid(String websiteAddress) {
        if(!AppUtils.isValidUri(websiteAddress))
            throw new IllegalArgumentException(INVALID_WEBSITE_ADDRESS);
    }

    private AuthorResponse buildAuthorResponse(Author author) {
        List<Member> members = author.getMembers();
        return AuthorResponse.builder()
                .authorId(author.getId())
                .members(members.stream()
                        .map(member -> userMapper.mapToUserResponse(member.getUser()))
                        .toList())
                .organizationPhoneNumber(author.getOrganizationPhoneNumber())
                .websiteAddress(author.getWebsiteAddress())
                .build();
    }

    @Override
    public PaginationResponse<AuthorResponse> getAllAuthor(PaginationRequest request) {
        Pageable pageable = PageRequestFactory.create(request, ALLOWED_SORT_FIELDS);
        Page<Author> authors = authorRepository.findAll(pageable);
        log.info("==>> Fetching all authors");
        return PageMapper.map(authors, this::buildAuthorResponse);
    }
}