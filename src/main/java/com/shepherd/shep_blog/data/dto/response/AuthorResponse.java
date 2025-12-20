package com.shepherd.shep_blog.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthorResponse {
    private UUID authorId;
    private List<UserResponse> members;
    private String organizationPhoneNumber;
    private String websiteAddress;
}
