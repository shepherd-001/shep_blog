package com.shepherd.shep_blog.services.auth;

import com.shepherd.shep_blog.data.dto.request.ChangePasswordRequest;
import com.shepherd.shep_blog.data.dto.request.LoginRequest;
import com.shepherd.shep_blog.data.dto.request.ResetPasswordRequest;
import com.shepherd.shep_blog.data.dto.request.VerifyEmailRequest;
import com.shepherd.shep_blog.data.dto.response.AuthResponse;
import com.shepherd.shep_blog.data.dto.response.VerifyEmailResponse;
import com.shepherd.shep_blog.data.model.TokenEntity;
import com.shepherd.shep_blog.data.model.TokenType;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.data.repository.UserRepository;
import com.shepherd.shep_blog.exceptions.ResourceNotFoundException;
import com.shepherd.shep_blog.exceptions.UserAlreadyEnabledException;
import com.shepherd.shep_blog.mapper.UserMapper;
import com.shepherd.shep_blog.security.AuthenticatedUser;
import com.shepherd.shep_blog.security.JwtUtils;
import com.shepherd.shep_blog.security.SecurityUtils;
import com.shepherd.shep_blog.services.JwtTokenService;
import com.shepherd.shep_blog.services.notification.MailNotificationService;
import com.shepherd.shep_blog.services.token.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.shepherd.shep_blog.utils.ErrorMessage.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final MailNotificationService  notificationService;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public VerifyEmailResponse verifyEmail(VerifyEmailRequest request) {
        TokenEntity tokenEntity = tokenService.validateToken(request.getToken(), request.getTokenType(), request.getEmail());
        User user = getUserByEmail(tokenEntity.getEmail());

        if(user.isEmailVerified())
            throw new UserAlreadyEnabledException("User is already verified");
       if (user.isEnabled())
            throw new UserAlreadyEnabledException("User is already enabled");

        user.setEnabled(true);
        user.setEmailVerified(true);

        user = userRepository.save(user);
        return userMapper.mapToVerifyEmailResponse(user, generateJwtToken(user));
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        User user = authenticatedUser.getUser();
        return generateJwtToken(user);
    }

    private AuthResponse generateJwtToken(User user) {
        String userEmail = user.getEmail();
        String accessToken = jwtUtils.generateAccessToken(userEmail);
        String refreshToken = jwtUtils.generateRefreshToken(userEmail);

        jwtTokenService.storeAccessToken(jwtUtils.getJwtId(accessToken), userEmail);
        jwtTokenService.storeRefreshToken(refreshToken, userEmail);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = SecurityUtils.getCurrentPrincipal().getUser();

        validatePasswordChange(user.getPassword(), changePasswordRequest);
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        log.info("==>> Password changed successfully");
        return generateJwtToken(user);
    }

    private void validatePasswordChange(String currentEncodedPassword, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), currentEncodedPassword))
            throw new BadCredentialsException(INVALID_CURRENT_PASSWORD);

        if (request.getCurrentPassword().equals(request.getNewPassword()))
            throw new BadCredentialsException(SAME_OLD_AND_NEW_PASSWORD);

        if (!request.getNewPassword().equals(request.getConfirmPassword()))
            throw new BadCredentialsException(MISMATCH_PASSWORD);
    }

    @Override
    public String requestPasswordReset(String email) {
        userRepository.findByEmailEqualsIgnoreCase(email.trim())
                .filter(user -> user.isEnabled() && user.isEmailVerified())
                .ifPresent(this::sendPasswordResetToken);
        return "If the email exists, a reset password link has been sent to your email address";
    }

    private void sendPasswordResetToken(User user) {
        String token = tokenService.generateToken(user.getEmail(), TokenType.RESET_PASSWORD);
        notificationService.sendResetPasswordMail(user, token);
        log.info("==>> Password reset email sent to: {}", user.getEmail());
    }

    @Override
    public AuthResponse resetPassword(ResetPasswordRequest request) {
        TokenEntity tokenEntity = tokenService.validateToken(request.getToken(),
                TokenType.RESET_PASSWORD, request.getEmail());
        User user = getUserByEmail(tokenEntity.getEmail());

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("==>> Password reset successful for user {}", user.getEmail());
        return generateJwtToken(user);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmailEqualsIgnoreCase(email.trim()).orElseThrow(
                ()-> new ResourceNotFoundException(USER_EMAIL_NOT_FOUND));
    }
}