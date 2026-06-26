package com.shepherd.shep_blog.controllers;

import com.shepherd.shep_blog.data.dto.request.*;
import com.shepherd.shep_blog.common.response.ApiResponse;
import com.shepherd.shep_blog.security.LogoutService;
import com.shepherd.shep_blog.services.auth.AuthService;
import com.shepherd.shep_blog.utils.RegexPattern;
import com.shepherd.shep_blog.utils.ValidationMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {
    private final AuthService authService;
    private final LogoutService logoutService;


    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<?>> verifyEmail(@Valid @RequestBody VerifyEmailRequest verifyEmailRequest) {
        return ResponseEntity.ok(ApiResponse
                .success("Email verified successfully", authService.verifyEmail(verifyEmailRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(ApiResponse
                .success("User authentication successful", authService.login(loginRequest)));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(ApiResponse
                .success("Password changed successfully", authService.changePassword(changePasswordRequest)));
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<ApiResponse<?>> requestPasswordReset(@RequestParam
                                                               @NotBlank(message = ValidationMessage.BLANK_EMAIL)
                                                               @Pattern(message = ValidationMessage.INVALID_EMAIL, regexp = RegexPattern.EMAIL)
                                                               String email) {
        return ResponseEntity.ok(ApiResponse
                .success(authService.requestPasswordReset(email)));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok(ApiResponse
                .success("Password reset successful", authService.resetPassword(resetPasswordRequest)));
    }

    @GetMapping("/user-detail")
    public ResponseEntity<ApiResponse<?>> viewUserDetails(){
        return ResponseEntity.ok(ApiResponse.success(authService.getAuthenticatedUser()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok(ApiResponse.success("Auth token refreshed successfully",
                authService.refreshToken(request.getRefreshToken())));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logoutCurrentSession(HttpServletRequest request) {
        logoutService.logoutCurrentSession(request);
        return ResponseEntity.ok(ApiResponse
                .success("User logged out successfully"));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<?>> logoutAllSessions() {
        logoutService.logoutAllSessions();
        return ResponseEntity.ok(ApiResponse
                .success("User logged out from all sessions"));
    }
}