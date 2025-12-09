package com.shepherd.shep_blog.data.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VerifyEmailResponse {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private boolean isEmailVerified;
    private boolean isEnabled;
    private String accessToken;
    private String refreshToken;
}