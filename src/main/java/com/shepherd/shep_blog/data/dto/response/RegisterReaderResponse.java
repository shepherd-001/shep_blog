package com.shepherd.shep_blog.data.dto.response;

import com.shepherd.shep_blog.data.model.Gender;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class RegisterReaderResponse {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private boolean enabled;
    private boolean emailVerified;
}
