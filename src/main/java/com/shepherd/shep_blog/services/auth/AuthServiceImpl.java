package com.shepherd.shep_blog.services.auth;

import com.shepherd.shep_blog.data.dto.request.LoginRequest;
import com.shepherd.shep_blog.data.dto.response.AuthResponse;
import com.shepherd.shep_blog.data.model.User;
import com.shepherd.shep_blog.security.AuthenticatedUser;
import com.shepherd.shep_blog.security.JwtUtils;
import com.shepherd.shep_blog.security.securityConfig.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final JwtTokenService jwtTokenService;

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
}