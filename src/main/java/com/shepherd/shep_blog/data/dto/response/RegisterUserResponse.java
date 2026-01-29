package com.shepherd.shep_blog.data.dto.response;

import com.shepherd.shep_blog.data.model.enums.Gender;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterUserResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private boolean enabled;
    private boolean emailVerified;
}
