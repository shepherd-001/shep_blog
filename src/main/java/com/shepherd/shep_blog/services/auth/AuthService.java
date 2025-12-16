package com.shepherd.shep_blog.services.auth;

import com.shepherd.shep_blog.data.dto.request.ChangePasswordRequest;
import com.shepherd.shep_blog.data.dto.request.LoginRequest;
import com.shepherd.shep_blog.data.dto.request.ResetPasswordRequest;
import com.shepherd.shep_blog.data.dto.request.VerifyEmailRequest;
import com.shepherd.shep_blog.data.dto.response.AuthResponse;
import com.shepherd.shep_blog.data.dto.response.UserResponse;
import com.shepherd.shep_blog.data.dto.response.VerifyEmailResponse;

public interface AuthService {
    VerifyEmailResponse verifyEmail(VerifyEmailRequest verifyEmailRequest);
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse changePassword(ChangePasswordRequest changePasswordRequest);
    String requestPasswordReset(String email);
    AuthResponse resetPassword(ResetPasswordRequest resetPasswordRequest);
    UserResponse getAuthenticatedUser();
}