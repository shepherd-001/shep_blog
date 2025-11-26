package com.shepherd.shep_blog.controllers;

import com.shepherd.shep_blog.data.dto.request.LoginRequest;
import com.shepherd.shep_blog.data.dto.response.ApiResponse;
import com.shepherd.shep_blog.security.LogoutService;
import com.shepherd.shep_blog.services.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {
    private final AuthService authService;
    private final LogoutService logoutService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(ApiResponse
                .success("User logged in successfully", authService.login(loginRequest), HttpStatus.OK));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logoutCurrentSession(HttpServletRequest request) {
        logoutService.logoutCurrentSession(request);
        return ResponseEntity.ok(ApiResponse
                .success("User logged out successfully", HttpStatus.OK));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<?>> logoutAllSessions() {
        logoutService.logoutAllSessions();
        return ResponseEntity.ok(ApiResponse
                .success("User logged out from all sessions", HttpStatus.OK));
    }
}