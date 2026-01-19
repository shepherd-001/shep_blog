package com.shepherd.shep_blog.data.dto.request;

import com.shepherd.shep_blog.data.model.enums.Gender;
import com.shepherd.shep_blog.utils.RegexPattern;
import com.shepherd.shep_blog.utils.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterReaderRequest {
    @NotBlank(message = ValidationMessage.BLANK_USER_NAME)
    @Pattern(message = ValidationMessage.INVALID_USER_NAME, regexp = RegexPattern.USER_NAME)
    private String username;

    @NotBlank(message = ValidationMessage.BLANK_EMAIL)
    @Pattern(message = ValidationMessage.INVALID_EMAIL, regexp = RegexPattern.EMAIL)
    private String email;

    @NotBlank(message = ValidationMessage.BLANK_PASSWORD)
    @Pattern(regexp = RegexPattern.PASSWORD, message = ValidationMessage.INVALID_PASSWORD)
    private String password;

    @NotNull(message = ValidationMessage.NULL_GENDER)
    private Gender gender;
}
