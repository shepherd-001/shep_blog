package com.shepherd.shep_blog.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.shepherd.shep_blog.utils.ValidationMessage.BLANK_EMAIL;
import static com.shepherd.shep_blog.utils.ValidationMessage.BLANK_TOKEN;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerifyEmailRequest {
    @NotBlank(message = BLANK_EMAIL)
    private String email;

    @NotBlank(message = BLANK_TOKEN)
    private String token;
}
