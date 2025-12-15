package com.shepherd.shep_blog.services.admin;

import com.shepherd.shep_blog.data.dto.request.AcceptInviteRequest;
import com.shepherd.shep_blog.data.dto.request.DeclineInviteRequest;
import com.shepherd.shep_blog.data.dto.response.InvitationResponse;
import com.shepherd.shep_blog.data.model.*;
import com.shepherd.shep_blog.data.repository.AdminRepository;
import com.shepherd.shep_blog.data.repository.InvitationRepository;
import com.shepherd.shep_blog.exceptions.ResourceNotFoundException;
import com.shepherd.shep_blog.exceptions.UnauthorizedException;
import com.shepherd.shep_blog.services.token.TokenService;
import com.shepherd.shep_blog.services.userRoleAndPermission.RoleService;
import com.shepherd.shep_blog.utils.RoleUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.shepherd.shep_blog.utils.ErrorMessage.INVITATION_ALREADY_PROCESSED;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService{
    private final AdminRepository adminRepository;
    private final TokenService tokenService;
    private final InvitationRepository  invitationRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public InvitationResponse declineInvitation(DeclineInviteRequest request) {
        TokenEntity tokenEntity = tokenService.validateToken(request.getToken(), request.getTokenType(), request.getEmail());
        String email = tokenEntity.getEmail();

        Invitation invitation = getInvitationByEmail(email);

        if(invitation.getStatus() != InvitationStatus.PENDING){
            log.warn("Attempt to decline non-pending invitation for '{}'", email);
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
            log.warn("Attempt to accept non-pending invitation for '{}'", email);
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

    private User createUser(AcceptInviteRequest request, User user) {
        UserRole userRole = roleService.getRole(RoleUtil.ADMIN);
        if(!user.getRole().equals(userRole)){
            throw new UnauthorizedException("You are not authorized to accept invitation");
        }

        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setUserName(request.getUserName().toLowerCase().trim());
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