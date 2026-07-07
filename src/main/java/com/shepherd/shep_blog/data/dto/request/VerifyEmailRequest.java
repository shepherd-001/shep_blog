package com.shepherd.shep_blog.data.dto.request;

import com.shepherd.shep_blog.data.model.enums.TokenType;
import com.shepherd.shep_blog.utils.RegexPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.shepherd.shep_blog.utils.ValidationMessage.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerifyEmailRequest {
    @NotBlank(message = BLANK_EMAIL)
    @Pattern(message = INVALID_EMAIL, regexp = RegexPattern.EMAIL)
    private String email;

    @NotBlank(message = BLANK_TOKEN)
    private String token;

    @NotNull(message = "Token type is required")
    private TokenType tokenType;
}
