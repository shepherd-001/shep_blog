package com.shepherd.shep_blog.data.dto.response;

import com.shepherd.shep_blog.data.model.TeamMemberStatus;
import com.shepherd.shep_blog.data.model.enums.Gender;
import com.shepherd.shep_blog.data.model.enums.TeamMemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TeamMemberResponse {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Gender gender;
    private boolean enabled;
    private boolean emailVerified;
    private TeamMemberStatus status;
    private TeamMemberRole role;
}
