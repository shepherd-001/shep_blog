package com.shepherd.shep_blog.data.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResetPasswordRequest {
//    @NotBlank(message = ValidationMessage.BLANK_EMAIL)
//    @Email(regexp = RegexPattern.EMAIL, message = ValidationMessage.INVALID_EMAIL)
    private String email;

//    @NotBlank(message = ValidationMessage.BLANK_TOKEN)
    private String token;

//    @NotBlank(message = ValidationMessage.BLANK_PASSWORD)
//    @Pattern(message = ValidationMessage.INVALID_PASSWORD, regexp = RegexPattern.PASSWORD)
//    private String newPassword;
}
