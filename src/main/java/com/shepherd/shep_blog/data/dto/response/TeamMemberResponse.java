package com.shepherd.shep_blog.data.dto.response;

import com.shepherd.shep_blog.data.model.UserRole;
import com.shepherd.shep_blog.data.model.enums.Gender;
import com.shepherd.shep_blog.data.model.enums.TeamMemberRole;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class TeamMemberResponse {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Gender gender;
//    private Set<UserRole> roles;
    private boolean enabled;
    private boolean emailVerified;
    private boolean revoked;
    private TeamMemberRole role;
}
