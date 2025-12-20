package com.shepherd.shep_blog.data.dto.response;

import com.shepherd.shep_blog.data.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
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
