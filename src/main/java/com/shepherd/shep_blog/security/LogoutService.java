package com.shepherd.shep_blog.security;

import com.shepherd.shep_blog.common.exceptions.InvalidJwtException;
import com.shepherd.shep_blog.services.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService{
    private final JwtUtils jwtUtils;
    private final JwtTokenService jwtTokenService;

    public void logoutCurrentSession(HttpServletRequest request){
        String token = SecurityUtils.extractJwtToken(request);
        String userEmail = SecurityUtils.getAuthenticationName();

        if(!jwtUtils.isValidToken(token, userEmail)){
            log.warn("==>> Invalid JWT provided for user {}", userEmail);
            throw new InvalidJwtException("Invalid or expired token");
        }

        String jwtId = jwtUtils.getJwtId(token);
        Instant expiration = jwtUtils.getExpiration(token);
        long remainingSeconds = Duration.between(Instant.now(), expiration).getSeconds();

        if(remainingSeconds > 0){
            jwtTokenService.blacklistAccessToken(jwtId);
            log.info("==>> User {} logged out, token blacklisted for {} seconds", userEmail, remainingSeconds);
        }
        else log.info("==>> Token already expired, no need for blacklist");
    }

    public void logoutAllSessions(){
        String userEmail = SecurityUtils.getAuthenticationName();
        jwtTokenService.revokeAllTokensForUser(userEmail);
        log.info("==>> All sessions logged out for user {}", userEmail);
    }
}