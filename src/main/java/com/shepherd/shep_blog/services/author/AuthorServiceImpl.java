package com.shepherd.shep_blog.services.author;

import com.shepherd.shep_blog.data.dto.request.CreateTeamMemberRequest;
import com.shepherd.shep_blog.data.dto.request.InviteTeamMemberRequest;
import com.shepherd.shep_blog.common.request.PaginationRequest;
import com.shepherd.shep_blog.data.dto.request.RegisterAuthorRequest;
import com.shepherd.shep_blog.data.dto.response.AuthorResponse;
import com.shepherd.shep_blog.common.response.PaginationResponse;
import com.shepherd.shep_blog.data.dto.response.TeamMemberResponse;
import com.shepherd.shep_blog.data.model.Author;
import com.shepherd.shep_blog.data.model.TeamMember;
import com.shepherd.shep_blog.data.model.TeamMemberStatus;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.data.model.enums.TeamMemberRole;
import com.shepherd.shep_blog.data.model.enums.TokenType;
import com.shepherd.shep_blog.data.repository.AuthorRepository;
import com.shepherd.shep_blog.data.repository.TeamMemberRepository;
import com.shepherd.shep_blog.common.exceptions.AlreadyExistsException;
import com.shepherd.shep_blog.common.exceptions.ResourceNotFoundException;
import com.shepherd.shep_blog.common.exceptions.UnauthorizedException;
import com.shepherd.shep_blog.mapper.AuthorMapper;
import com.shepherd.shep_blog.mapper.UserMapper;
import com.shepherd.shep_blog.security.SecurityUtils;
import com.shepherd.shep_blog.services.notification.MailNotificationService;
import com.shepherd.shep_blog.services.token.TokenService;
import com.shepherd.shep_blog.services.user.UserService;
import com.shepherd.shep_blog.services.userRoleAndPermission.RoleService;
import com.shepherd.shep_blog.utils.AppUtils;
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
import static com.shepherd.shep_blog.utils.ErrorMessage.TEAM_MEMBER_NOT_FOUND;
import static com.shepherd.shep_blog.utils.RoleUtil.SUPER_AUTHOR;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthorMapper authorMapper;
    private final TokenService tokenService;
    private final TeamMemberRepository teamMemberRepository;
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
        user.assignRole(roleService.getRole(SUPER_AUTHOR));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userService.saveUser(user);

        TeamMember teamMember = TeamMember.builder()
                .user(user)
                .role(TeamMemberRole.OWNER)
                .status(TeamMemberStatus.INVITED)
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
        if (!AppUtils.isValidUri(websiteAddress))
            throw new IllegalArgumentException(INVALID_WEBSITE_ADDRESS);
    }

    private AuthorResponse buildAuthorResponse(Author author) {
        List<TeamMember> teamMembers = author.getTeamMembers();
        return AuthorResponse.builder()
                .authorId(author.getId())
                .members(teamMembers.stream()
                        .map(authorMapper::mapToTeamMemberResponse)
                        .toList())
                .organizationPhoneNumber(author.getOrganizationPhoneNumber())
                .websiteAddress(author.getWebsiteAddress())
                .build();
    }

    @Override
    @Cacheable(
            value = AUTHOR_CACHE_NAME,
            key = "#request.toCacheKey('authors')",
            unless = "#result == null || #result.items.isEmpty() || #request.page() > 5"
    )
    public PaginationResponse<AuthorResponse> getAllAuthor(PaginationRequest request) {
        Pageable pageable = request.toPageable(ALLOWED_SORT_FIELDS);
        Page<Author> authors = authorRepository.findAll(pageable);
        log.info("==>> Fetching all authors");
        return PaginationResponse.map(authors, this::buildAuthorResponse);
    }

    @Transactional
    @CacheEvict(value = AUTHOR_CACHE_NAME, key = "'author:' + #request.authorId")
    @Override
    public TeamMemberResponse inviteTeamMember(InviteTeamMemberRequest request) {
        Author author = getAuthorById(request.getAuthorId());

        User sender = SecurityUtils.getCurrentPrincipal().getUser();
        authorizeInvite(author.getId(), sender.getId());
        checkIfTeamMemberEmailExists(author.getId(), request.getEmail());
        validateTeamMemberRole(author.getId(), request.getRole());

        User invitedUser = userService.getByEmailIgnoreCase(request.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User must exist before being invited"));
        invitedUser = userService.saveUser(invitedUser);

        TeamMember teamMember = TeamMember.builder()
                .user(invitedUser)
                .role(request.getRole())
                .status(TeamMemberStatus.INVITED)
                .build();
        author.addMember(teamMember);
        authorRepository.save(author);

        sendAuthorMemberInvitation(sender, teamMember);
        return authorMapper.mapToTeamMemberResponse(teamMember);
    }

    private void sendAuthorMemberInvitation(User sender, TeamMember teamMember) {
        String senderName = buildSenderName(sender);
        User invitedUser = teamMember.getUser();
        TokenType tokenType = TokenType.AUTHOR_MEMBER_INVITATION;
        String token = tokenService.generateToken(invitedUser.getEmail(), tokenType);
        String role = teamMember.getRole().name();
        notificationService.sendAuthorMemberInvitation(invitedUser, token, senderName, role, tokenType);
    }

    @Transactional
    @CacheEvict(value = AUTHOR_CACHE_NAME, key = "'author:' + #request.authorId")
    @Override
    public TeamMemberResponse createTeamMember(CreateTeamMemberRequest request) {
        Author author = getAuthorById(request.getAuthorId());
        userService.checkIfUserEmailExists(request.getEmail());

        User sender = SecurityUtils.getCurrentPrincipal().getUser();
        authorizeInvite(author.getId(), sender.getId());
        checkIfTeamMemberEmailExists(author.getId(), request.getEmail());
        validateTeamMemberRole(author.getId(), request.getRole());

        User user = userMapper.mapToUser(request);
        user = userService.saveUser(user);

        TeamMember teamMember = TeamMember.builder()
                .user(user)
                .role(request.getRole())
                .status(TeamMemberStatus.INVITED)
                .build();

        author.addMember(teamMember);
        authorRepository.save(author);

        TokenType tokenType = TokenType.EMAIL_CONFIRMATION;
        String token = tokenService.generateToken(user.getEmail(), tokenType);
        notificationService.sendVerificationMail(user, token, tokenType);
        return authorMapper.mapToTeamMemberResponse(teamMember);
    }

    private Author getAuthorById(UUID authorId) {
        return authorRepository.findById(authorId).orElseThrow(
                () -> new ResourceNotFoundException("Author not found"));
    }

    private void authorizeInvite(UUID authorId, UUID userId) {
        if (!teamMemberRepository.existsByAuthorIdAndUserIdAndStatus(authorId, userId, TeamMemberStatus.ACTIVE))
            throw new UnauthorizedException("User is not allowed to invite members to this author");
    }

    private void validateTeamMemberRole(UUID authorId, TeamMemberRole role) {
        if (role == TeamMemberRole.OWNER &&
                authorRepository.existsByIdAndTeamMembers_Role(authorId, TeamMemberRole.OWNER)) {
            throw new IllegalArgumentException(
                    "Role 'OWNER' already exists for this author"
            );
        }
    }

    private void checkIfTeamMemberEmailExists(UUID authorId, String email) {
        if (teamMemberRepository.existsByAuthorIdAndUserEmailIgnoreCase(authorId, email.trim())) {
            throw new AlreadyExistsException("The user is already a member of this author team");
        }
    }

    private String buildSenderName(User sender) {
        return (sender.getFirstName() == null || sender.getLastName() == null)
                ? "Team Admin"
                : String.format("%s %s", sender.getFirstName(), sender.getLastName());
    }

    @CacheEvict(value = AUTHOR_CACHE_NAME, key = "'author:' + #authorId")
    @Override
    public TeamMemberResponse activateTeamMember(UUID authorId) {
        User currentUser = SecurityUtils.getCurrentPrincipal().getUser();

        TeamMember teamMember = getTeamMember(authorId, currentUser.getId());
        activate(teamMember);
        teamMember = teamMemberRepository.save(teamMember);
        return authorMapper.mapToTeamMemberResponse(teamMember);
    }

    private TeamMember getTeamMember(UUID authorId, UUID userId) {
        return teamMemberRepository.findByAuthor_IdAndUser_Id(authorId, userId).orElseThrow(
                () -> new ResourceNotFoundException(TEAM_MEMBER_NOT_FOUND));
    }

    private void activate(TeamMember teamMember) {
        TeamMemberStatus status = teamMember.getStatus();
        if (status == TeamMemberStatus.ACTIVE) {
            return;
        }
        if (status != TeamMemberStatus.INVITED) {
            throw new IllegalStateException("Invalid activation status");
        }
        teamMember.setStatus(TeamMemberStatus.ACTIVE);
    }

//    private void validateTeamMemberStatus(TeamMember teamMember){
//        if (teamMember.getStatus() != TeamMemberStatus.ACTIVE) {
//            throw new UnauthorizedException("Team member is not active");
//        }
//    }
}
