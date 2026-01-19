package com.shepherd.shep_blog.services.admin;

import com.shepherd.shep_blog.data.dto.AdminResponse;
import com.shepherd.shep_blog.data.dto.request.AcceptInviteRequest;
import com.shepherd.shep_blog.data.dto.request.DeclineInviteRequest;
import com.shepherd.shep_blog.data.dto.request.PaginationRequest;
import com.shepherd.shep_blog.data.dto.response.InvitationResponse;
import com.shepherd.shep_blog.data.dto.response.PaginationResponse;
import com.shepherd.shep_blog.data.model.Admin;
import com.shepherd.shep_blog.data.model.Invitation;
import com.shepherd.shep_blog.data.model.TokenEntity;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.data.model.enums.InvitationStatus;
import com.shepherd.shep_blog.data.model.enums.TokenType;
import com.shepherd.shep_blog.data.repository.AdminRepository;
import com.shepherd.shep_blog.data.repository.InvitationRepository;
import com.shepherd.shep_blog.exceptions.ResourceNotFoundException;
import com.shepherd.shep_blog.exceptions.UnauthorizedException;
import com.shepherd.shep_blog.mapper.UserMapper;
import com.shepherd.shep_blog.services.token.TokenService;
import com.shepherd.shep_blog.services.user.UserService;
import com.shepherd.shep_blog.utils.RoleUtil;
import com.shepherd.shep_blog.utils.pagination_utils.PageMapper;
import com.shepherd.shep_blog.utils.pagination_utils.PageRequestFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.shepherd.shep_blog.utils.ErrorMessage.INVITATION_ALREADY_PROCESSED;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService{
    private final AdminRepository adminRepository;
    private final TokenService tokenService;
    private final InvitationRepository  invitationRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private static final Set<String> ALLOWED_FIELDS = Set.of("createdAt", "createdBy");
    private final UserService userService;

    @Override
    public InvitationResponse declineInvitation(DeclineInviteRequest request) {
        TokenEntity tokenEntity = tokenService.validateToken(request.getToken(), request.getTokenType(), request.getEmail());
        String email = tokenEntity.getEmail();

        Invitation invitation = getInvitationByEmail(email);

        if(invitation.getStatus() != InvitationStatus.PENDING){
            log.warn("==>> Attempt to decline non-pending invitation for '{}'", email);
            throw new IllegalStateException(INVITATION_ALREADY_PROCESSED);
        }

        invitationRepository.delete(invitation);

        return InvitationResponse.builder()
                .status(InvitationStatus.DECLINED)
                .build();
    }

    @Override
    @Transactional
    public InvitationResponse acceptInvitation(AcceptInviteRequest request) {
        TokenEntity tokenEntity = tokenService.validateToken(request.getToken(), TokenType.ADMIN_INVITATION, request.getEmail());
        String email = tokenEntity.getEmail();

        Invitation invitation = getInvitationByEmail(email);

        if(invitation.getStatus() != InvitationStatus.PENDING){
            log.warn("==>> Attempt to accept non-pending invitation for '{}'", email);
            throw new IllegalStateException(INVITATION_ALREADY_PROCESSED);
        }

        User user = createUser(request, invitation.getUser());

        Admin admin = Admin.builder()
                .user(user)
                .build();
        adminRepository.save(admin);

        invitationRepository.delete(invitation);
        return InvitationResponse.builder()
                .status(InvitationStatus.ACCEPTED)
                .build();
    }

    @Override
    public PaginationResponse<AdminResponse> getAllActiveAdmins(PaginationRequest request) {
        Pageable pageable = PageRequestFactory.create(request, ALLOWED_FIELDS);
        Page<Admin> admins = adminRepository.findAllActiveAdmins(pageable);
        log.info("==>> Fetching all admins");
        return PageMapper.map(admins, this::buildAdminResponse);
    }

    private AdminResponse buildAdminResponse(Admin admin){
        return AdminResponse.builder()
                .user(userMapper.mapToUserResponse(admin.getUser()))
                .build();
    }

    private User createUser(AcceptInviteRequest request, User user) {
        if(!userService.existsUserRole(user.getId(), RoleUtil.ADMIN)){
            throw new UnauthorizedException("You are not authorized to accept invitation");
        }

        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setUsername(request.getUserName().toLowerCase().trim());
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setGender(request.getGender());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setEmailVerified(true);
        return user;
    }

    private Invitation getInvitationByEmail(String email){
        return invitationRepository.findByUserEmailEqualsIgnoreCase(email)
                .orElseThrow(()-> new ResourceNotFoundException("Invitation not found"));
    }
}