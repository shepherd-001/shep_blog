package com.shepherd.shep_blog.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {
//    @NotBlank(message = ValidationMessage.BLANK_EMAIL)
//    @Email(message = ValidationMessage.INVALID_EMAIL, regexp = RegexPattern.EMAIL)
    private String email;
//    @NotBlank(message = ValidationMessage.BLANK_PASSWORD)
//    @Pattern(regexp = RegexPattern.PASSWORD, message = ValidationMessage.INVALID_PASSWORD)
    private String password;
}
