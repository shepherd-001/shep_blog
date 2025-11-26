package com.shepherd.shep_blog.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {
//    @NotBlank(message = ValidationMessage.BLANK_PASSWORD)
//    @Pattern(message = ValidationMessage.INVALID_PASSWORD, regexp = RegexPattern.PASSWORD)
    private String currentPassword;

//    @NotBlank(message = ValidationMessage.BLANK_PASSWORD)
//    @Pattern(message = ValidationMessage.INVALID_PASSWORD, regexp = RegexPattern.PASSWORD)
    private String newPassword;

//    @NotBlank(message = ValidationMessage.BLANK_PASSWORD)
//    @Pattern(message = ValidationMessage.INVALID_PASSWORD, regexp = RegexPattern.PASSWORD)
    private String confirmPassword;
}
