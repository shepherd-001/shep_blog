package com.shepherd.shep_blog.data.dto.request;

import com.shepherd.shep_blog.data.model.enums.TeamMemberRole;
import com.shepherd.shep_blog.utils.RegexPattern;
import com.shepherd.shep_blog.utils.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InviteTeamMemberRequest {
    private UUID authorId;

    @NotBlank(message = ValidationMessage.BLANK_EMAIL)
    @Pattern(message = ValidationMessage.INVALID_EMAIL, regexp = RegexPattern.EMAIL)
    private String email;

    private TeamMemberRole  role;
}
