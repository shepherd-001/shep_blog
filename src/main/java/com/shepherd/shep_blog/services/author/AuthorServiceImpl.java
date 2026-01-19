package com.shepherd.shep_blog.services.author;

import com.shepherd.shep_blog.data.dto.request.AddTeamMemberRequest;
import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.dto.response.AuthorResponse;
import com.shepherd.shep_blog.data.dto.response.PaginationResponse;
import com.shepherd.shep_blog.data.model.Author;
import com.shepherd.shep_blog.data.model.TeamMember;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.data.model.enums.TokenType;
import com.shepherd.shep_blog.data.repository.AuthorRepository;
import com.shepherd.shep_blog.data.repository.TeamMemberRepository;
import com.shepherd.shep_blog.exceptions.AlreadyExistsException;
import com.shepherd.shep_blog.exceptions.ResourceNotFoundException;
import com.shepherd.shep_blog.exceptions.UnauthorizedException;
import com.shepherd.shep_blog.mapper.UserMapper;
import com.shepherd.shep_blog.security.SecurityUtils;
import com.shepherd.shep_blog.services.notification.MailNotificationService;
import com.shepherd.shep_blog.services.token.TokenService;
import com.shepherd.shep_blog.services.user.UserService;
import com.shepherd.shep_blog.services.userRoleAndPermission.RoleService;
import com.shepherd.shep_blog.utils.AppUtils;
import com.shepherd.shep_blog.utils.pagination_utils.PageMapper;
import com.shepherd.shep_blog.utils.pagination_utils.PageRequestFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    private final TeamMemberRepository  teamMemberRepository;
    private final MailNotificationService notificationService;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("organizationPhoneNumber", "createdAt");
    private static final String AUTHOR_CACHE_NAME = "authorCache";


    @Transactional
    @CacheEvict(value = AUTHOR_CACHE_NAME, allEntries = true)
    @Override
    public AuthorResponse registerAuthor(RegisterAuthorRequest request) {
        userService.checkIfUserEmailExists(request.getEmail());
        userService.checkIfUserNameExists(request.getUsername());
        checkIfWebsiteAddressIsValid(request.getWebsiteAddress());

        User user = userMapper.mapToUser(request);
        user.setRoles(Set.of(roleService.getRole(SUPER_AUTHOR)));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userService.saveUser(user);

        TeamMember teamMember = TeamMember.builder()
                .user(user)
                .build();

        Author author = Author.builder()
                .organizationPhoneNumber(request.getOrganizationPhoneNumber().trim())
                .websiteAddress(request.getWebsiteAddress().trim().toLowerCase())
                .build();
        author.addMember(teamMember);
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
        List<TeamMember> teamMembers = author.getTeamMembers();
        return AuthorResponse.builder()
                .authorId(author.getId())
                .members(teamMembers.stream()
                        .map(teamMember -> userMapper.mapToUserResponse(teamMember.getUser()))
                        .toList())
                .organizationPhoneNumber(author.getOrganizationPhoneNumber())
                .websiteAddress(author.getWebsiteAddress())
                .build();
    }

    @Override
    @Cacheable(
            value = AUTHOR_CACHE_NAME,
            key = "#request.toCacheKey('authors')",
            unless = "#result == null || #result.content.isEmpty() || #request.resolvedPageNumber() > 5"
    )
    public PaginationResponse<AuthorResponse> getAllAuthor(PaginationRequest request) {
        Pageable pageable = PageRequestFactory.create(request, ALLOWED_SORT_FIELDS);
        Page<Author> authors = authorRepository.findAll(pageable);
        log.info("==>> Fetching all authors");
        return PageMapper.map(authors, this::buildAuthorResponse);
    }

    @Transactional
    @CacheEvict(value = AUTHOR_CACHE_NAME, allEntries = true)
    @Override
    public AuthorResponse addTeamMember(AddTeamMemberRequest request) {
        Author author = getAuthorById(request.getAuthorId());

        User sender = SecurityUtils.getCurrentPrincipal().getUser();
        authorizeInvite(author.getId(), sender.getId());

        checkIfTeamMemberEmailExists(author.getId(), request.getEmail());

        User user = userMapper.mapToUser(request);
//        user.setRole(roleService.getRole(AUTHOR));
        user = userService.saveUser(user);

        TeamMember teamMember = TeamMember.builder()
                .user(user)
                .build();

        author.addMember(teamMember);
        author = authorRepository.save(author);

        String senderName = buildSenderName(sender);

        TokenType tokenType = TokenType.AUTHOR_MEMBER_INVITATION;
        String token = tokenService.generateToken(user.getEmail(), tokenType);
        notificationService.sendAuthorMemberInvitation(user, token, senderName, tokenType);
        return buildAuthorResponse(author);
    }

    private Author getAuthorById(UUID authorId) {
        return authorRepository.findById(authorId).orElseThrow(
                ()-> new ResourceNotFoundException("Author not found"));
    }

    private void authorizeInvite(UUID authorId, UUID userId) {
        if(!teamMemberRepository.existsByAuthorIdAndUserId(authorId, userId))
            throw new UnauthorizedException("User is not allowed to invite members to this author");
    }

    private void checkIfTeamMemberEmailExists(UUID authorId, String email) {
        if(teamMemberRepository.existsByAuthorIdAndUserEmailIgnoreCase(authorId, email)){
            throw new AlreadyExistsException("The user is already a member of this author team");
        }
    }

    private String buildSenderName(User sender) {
        return (sender.getFirstName() == null || sender.getLastName() == null)
                ? "Team Admin"
                : String.format("%s %s", sender.getFirstName(), sender.getLastName());
    }
}


//@Transactional
//@CacheEvict(value = AUTHOR_CACHE_NAME, allEntries = true)
//@Override
//public AuthorResponse addTeamMember(AddTeamMemberRequest request) {
//    Author author = getAuthorById(request.getAuthorId());
//
//    User sender = SecurityUtils.getCurrentPrincipal().getUser();
//    authorizeInvite(author.getId(), sender.getId());
//
//    // 1. Find existing user (global)
//    User user = userService.findByEmailIgnoreCase(request.getEmail())
//            .orElseGet(() -> createNewUser(request));
//
//    // 2. Check team membership
//    if (teamMemberRepository.existsByAuthorIdAndUserId(author.getId(), user.getId())) {
//        throw new AlreadyExistsException("The user is already a member of this author team");
//    }
//
//    // 3. Add team member
//    TeamMember teamMember = TeamMember.builder()
//            .user(user)
//            .author(author)
//            .build();
//
//    author.getTeamMembers().add(teamMember);
//    authorRepository.save(author);
//
//    // 4. Notify
//    sendInvitation(sender, user);
//
//    return buildAuthorResponse(author);
//}



//private User createNewUser(AddTeamMemberRequest request) {
//    User user = userMapper.mapToUser(request);
//    user.setRole(roleService.getRole(AUTHOR));
//    user.setEnabled(false);
//    user.setEmailVerified(false);
//    return userService.saveUser(user);
//}