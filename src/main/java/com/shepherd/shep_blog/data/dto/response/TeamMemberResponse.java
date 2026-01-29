package com.shepherd.shep_blog.data.dto.response;

import com.shepherd.shep_blog.data.model.UserRole;
import com.shepherd.shep_blog.data.model.enums.Gender;
import com.shepherd.shep_blog.data.model.enums.TeamMemberRole;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class TeamMemberResponse {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Gender gender;
    private boolean enabled;
    private boolean emailVerified;
    private boolean revoked;
    private TeamMemberRole role;
}
