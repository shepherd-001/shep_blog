package com.shepherd.shep_blog.data.dto.response;

import com.shepherd.shep_blog.data.model.Gender;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private Gender gender;
    private boolean enabled;
    private boolean emailVerified;
}
