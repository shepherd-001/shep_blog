package com.shepherd.shep_blog.data.dto.response;

import com.shepherd.shep_blog.data.model.UserRole;
import com.shepherd.shep_blog.data.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserResponse {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Gender gender;
    private Set<UserRole> roles;
    private boolean enabled;
    private boolean emailVerified;
}
