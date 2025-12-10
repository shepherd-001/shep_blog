package com.shepherd.shep_blog.data.dto.request;

import com.shepherd.shep_blog.utils.RegexPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InviteAdminRequest {
    @NotEmpty(message = "At least one user email is required")
    @Size(max = 10, message = "You can't invite more than 10 admins at once")
    private List<@NotBlank
    @Pattern(regexp = RegexPattern.EMAIL,
            message = "Each admin email must be valid")
            String> adminEmails;
}