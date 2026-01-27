package com.shepherd.shep_blog.data.dto.request;

import com.shepherd.shep_blog.data.model.enums.Gender;
import com.shepherd.shep_blog.data.model.enums.TeamMemberRole;
import com.shepherd.shep_blog.utils.RegexPattern;
import com.shepherd.shep_blog.utils.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddTeamMemberRequest {
    private UUID authorId;

    @NotBlank(message = ValidationMessage.BLANK_FIRST_NAME)
    @Pattern(message = ValidationMessage.INVALID_FIRST_NAME, regexp = RegexPattern.PERSON_NAME)
    private String firstName;

    @NotBlank(message = ValidationMessage.BLANK_LAST_NAME)
    @Pattern(message = ValidationMessage.INVALID_LAST_NAME, regexp = RegexPattern.PERSON_NAME)
    private String lastName;

    @NotBlank(message = ValidationMessage.BLANK_EMAIL)
    @Pattern(message = ValidationMessage.INVALID_EMAIL, regexp = RegexPattern.EMAIL)
    private String email;

    @NotNull(message = ValidationMessage.NULL_GENDER)
    private Gender gender;

    private TeamMemberRole  role;
}
