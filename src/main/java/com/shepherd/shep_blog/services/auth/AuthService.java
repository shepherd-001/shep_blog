package com.shepherd.shep_blog.services.auth;

import com.shepherd.shep_blog.data.dto.request.LoginRequest;
import com.shepherd.shep_blog.data.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
//    AuthResponse changePassword(ChangePasswordRequest changePasswordRequest);
//    String requestPasswordReset(String email);
//    AuthResponse resetPassword(ResetPasswordRequest resetPasswordRequest);
}