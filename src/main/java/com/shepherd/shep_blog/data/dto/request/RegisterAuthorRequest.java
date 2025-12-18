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
public class RegisterAuthorRequest {
    @NotBlank(message = ValidationMessage.BLANK_FIRST_NAME)
    @Pattern(message = ValidationMessage.INVALID_FIRST_NAME, regexp = RegexPattern.PERSON_NAME)
    private String firstName;

    @NotBlank(message = ValidationMessage.BLANK_LAST_NAME)
    @Pattern(message = ValidationMessage.INVALID_LAST_NAME, regexp = RegexPattern.PERSON_NAME)
    private String lastName;

    @NotBlank(message = ValidationMessage.BLANK_EMAIL)
    @Pattern(message = ValidationMessage.INVALID_USER_NAME, regexp = RegexPattern.USER_NAME)
    private String userName;

    @NotBlank(message = ValidationMessage.BLANK_EMAIL)
    @Pattern(message = ValidationMessage.INVALID_EMAIL, regexp = RegexPattern.EMAIL)
    private String email;

    @NotBlank(message = ValidationMessage.BLANK_PASSWORD)
    @Pattern(message = ValidationMessage.INVALID_PASSWORD, regexp = RegexPattern.PASSWORD)
    private String password;

    @NotNull(message = ValidationMessage.NULL_GENDER)
    private Gender gender;

    @NotBlank(message = ValidationMessage.BLANK_PHONE_NUMBER)
    @Pattern(message = "Organization phone number is invalid", regexp = RegexPattern.PHONE_NUMBER)
    private String organizationPhoneNumber;

    @NotBlank(message = ValidationMessage.BLANK_WEBSITE_ADDRESS)
    private String websiteAddress;
}
